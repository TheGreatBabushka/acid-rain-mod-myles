package com.thegreatbabushka.acidrain;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AcidRainEvent {
    private static final int DAMAGE_INTERVAL = 20; // Damage every second (20 ticks)
    private static final float DAMAGE_AMOUNT = 1.0f; // 1.0 damage (0.5 hearts)

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Player player = event.player;
        Level level = player.level();

        // Only run on server side
        if (level.isClientSide) {
            return;
        }

        // Check if it's raining and acid rain is enabled
        if (!level.isRaining() || !AcidRainState.isAcidRainActive((ServerLevel) level)) {
            return;
        }

        // Only damage at intervals using game tick
        if (player.tickCount % DAMAGE_INTERVAL != 0) {
            return;
        }

        // Check if player is exposed to the sky (not under a block)
        BlockPos playerPos = player.blockPosition();
        if (level.canSeeSky(playerPos)) {
            // Player is exposed to acid rain - deal damage
            DamageSource acidRainDamage = player.damageSources().magic();
            player.hurt(acidRainDamage, DAMAGE_AMOUNT);
        }
    }
}
