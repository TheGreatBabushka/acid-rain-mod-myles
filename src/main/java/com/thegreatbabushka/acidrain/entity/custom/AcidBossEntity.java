package com.thegreatbabushka.acidrain.entity.custom;

import com.thegreatbabushka.acidrain.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.UUID;

public class AcidBossEntity extends Monster {
    private static final EntityDataAccessor<Integer> SIZE = SynchedEntityData.defineId(AcidBossEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> MOUTH_OPEN = SynchedEntityData.defineId(AcidBossEntity.class, EntityDataSerializers.BOOLEAN);
    
    private int shootCooldown = 0;
    private int eatCooldown = 0;
    private UUID eatenPlayerUUID;
    private int eatTimer = 0;
    private static final int EAT_DURATION = 100; // 5 seconds at 20 ticks/second

    public AcidBossEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 50;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new AcidBossMeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SIZE, 2);
        this.entityData.define(MOUTH_OPEN, false);
    }

    public int getSize() {
        return this.entityData.get(SIZE);
    }

    public void setSize(int size) {
        this.entityData.set(SIZE, Mth.clamp(size, 1, 4));
        this.refreshDimensions();
    }

    public boolean isMouthOpen() {
        return this.entityData.get(MOUTH_OPEN);
    }

    public void setMouthOpen(boolean open) {
        this.entityData.set(MOUTH_OPEN, open);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide) {
            // Handle eaten player
            if (eatenPlayerUUID != null) {
                Player eatenPlayer = this.level().getPlayerByUUID(eatenPlayerUUID);
                if (eatenPlayer != null && eatenPlayer.isAlive()) {
                    // Keep player inside boss
                    eatenPlayer.teleportTo(this.getX(), this.getY() + 1, this.getZ());
                    eatenPlayer.setDeltaMovement(Vec3.ZERO);
                    eatenPlayer.fallDistance = 0;
                    
                    eatTimer++;
                    if (eatTimer >= EAT_DURATION) {
                        // Spit out player
                        Vec3 spitDirection = this.getLookAngle().scale(2.0);
                        eatenPlayer.teleportTo(
                            this.getX() + spitDirection.x,
                            this.getY() + 1,
                            this.getZ() + spitDirection.z
                        );
                        eatenPlayer.setDeltaMovement(spitDirection.x, 0.5, spitDirection.z);
                        eatenPlayer.hurt(this.damageSources().mobAttack(this), 4.0F);
                        
                        eatenPlayerUUID = null;
                        eatTimer = 0;
                        setMouthOpen(false);
                    }
                } else {
                    eatenPlayerUUID = null;
                    eatTimer = 0;
                    setMouthOpen(false);
                }
            }

            // Decrease cooldowns
            if (shootCooldown > 0) {
                shootCooldown--;
            }
            if (eatCooldown > 0) {
                eatCooldown--;
            }

            // Acid shooting attack
            LivingEntity target = this.getTarget();
            if (target != null && this.hasLineOfSight(target) && shootCooldown <= 0) {
                double distanceSq = this.distanceToSqr(target);
                if (distanceSq > 9.0D && distanceSq < 400.0D) { // Between 3 and 20 blocks
                    shootAcidProjectile(target);
                    shootCooldown = 60; // 3 second cooldown
                }
            }

            // Eat attack
            if (target instanceof Player player && eatCooldown <= 0 && eatenPlayerUUID == null) {
                double distance = this.distanceTo(target);
                if (distance < 3.0D) { // Within 3 blocks
                    tryToEatPlayer(player);
                }
            }
        }
    }

    private void shootAcidProjectile(LivingEntity target) {
        setMouthOpen(true);
        
        double d0 = target.getX() - this.getX();
        double d1 = target.getY(0.5D) - this.getY(0.5D);
        double d2 = target.getZ() - this.getZ();
        
        AcidProjectileEntity projectile = new AcidProjectileEntity(this.level(), this);
        projectile.setPos(this.getX(), this.getY() + 1.5D, this.getZ());
        projectile.shoot(d0, d1, d2, 1.5F, 1.0F);
        
        this.level().addFreshEntity(projectile);
        this.playSound(SoundEvents.GHAST_SHOOT, 1.0F, 1.0F);
        
        // Close mouth after a short delay
        this.level().getServer().execute(() -> {
            try {
                Thread.sleep(500);
                setMouthOpen(false);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    private void tryToEatPlayer(Player player) {
        if (player.isAlive() && !player.isCreative() && !player.isSpectator()) {
            eatenPlayerUUID = player.getUUID();
            eatTimer = 0;
            eatCooldown = 400; // 20 second cooldown
            setMouthOpen(true);
            
            this.playSound(SoundEvents.GENERIC_EAT, 1.0F, 0.8F);
            player.hurt(this.damageSources().mobAttack(this), 6.0F);
        }
    }

    @Override
    public boolean doHurtTarget(net.minecraft.world.entity.Entity pEntity) {
        boolean flag = super.doHurtTarget(pEntity);
        if (flag) {
            this.playSound(SoundEvents.SLIME_ATTACK, 1.0F, 0.8F);
        }
        return flag;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SLIME_SQUISH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.SLIME_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SLIME_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        this.playSound(SoundEvents.SLIME_SQUISH_SMALL, 0.15F, 1.0F);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Size", this.getSize());
        pCompound.putInt("ShootCooldown", this.shootCooldown);
        pCompound.putInt("EatCooldown", this.eatCooldown);
        pCompound.putInt("EatTimer", this.eatTimer);
        if (this.eatenPlayerUUID != null) {
            pCompound.putUUID("EatenPlayer", this.eatenPlayerUUID);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setSize(pCompound.getInt("Size"));
        this.shootCooldown = pCompound.getInt("ShootCooldown");
        this.eatCooldown = pCompound.getInt("EatCooldown");
        this.eatTimer = pCompound.getInt("EatTimer");
        if (pCompound.hasUUID("EatenPlayer")) {
            this.eatenPlayerUUID = pCompound.getUUID("EatenPlayer");
        }
    }

    // Custom melee attack goal that opens mouth
    static class AcidBossMeleeAttackGoal extends MeleeAttackGoal {
        private final AcidBossEntity acidBoss;

        public AcidBossMeleeAttackGoal(AcidBossEntity pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            this.acidBoss = pMob;
        }

        @Override
        public void start() {
            super.start();
            acidBoss.setMouthOpen(true);
        }

        @Override
        public void stop() {
            super.stop();
            acidBoss.setMouthOpen(false);
        }
    }
}
