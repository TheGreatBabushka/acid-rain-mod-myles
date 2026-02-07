package com.thegreatbabushka.acidrain.entity.custom;

import com.thegreatbabushka.acidrain.entity.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class AcidProjectileEntity extends ThrowableProjectile {
    public AcidProjectileEntity(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public AcidProjectileEntity(Level pLevel, LivingEntity pShooter) {
        super(ModEntities.ACID_PROJECTILE.get(), pShooter, pLevel);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            // Spawn acid particles
            for (int i = 0; i < 2; i++) {
                this.level().addParticle(ParticleTypes.ITEM_SLIME,
                        this.getX() + (this.random.nextDouble() - 0.5D) * 0.3D,
                        this.getY() + (this.random.nextDouble() - 0.5D) * 0.3D,
                        this.getZ() + (this.random.nextDouble() - 0.5D) * 0.3D,
                        0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (!this.level().isClientSide) {
            if (pResult.getEntity() instanceof LivingEntity livingEntity) {
                livingEntity.hurt(this.damageSources().mobProjectile(this, (LivingEntity) this.getOwner()), 6.0F);
                // Apply poison effect to represent acid damage over time
                livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0));
            }
        }
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (!this.level().isClientSide) {
            // Create acid splash effect
            for (int i = 0; i < 8; i++) {
                this.level().addParticle(ParticleTypes.ITEM_SLIME,
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        (this.random.nextDouble() - 0.5D) * 0.2D,
                        this.random.nextDouble() * 0.2D,
                        (this.random.nextDouble() - 0.5D) * 0.2D);
            }
            this.discard();
        }
    }
}
