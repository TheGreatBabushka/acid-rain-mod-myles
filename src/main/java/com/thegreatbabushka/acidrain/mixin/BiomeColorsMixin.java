package com.thegreatbabushka.acidrain.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin to change the water color (which affects rain particles) to green during acid rain
 */
@Mixin(BiomeColors.class)
public class BiomeColorsMixin {
    
    // Greenish color for acid rain effect (RGB: 64, 255, 64)
    private static final int ACID_RAIN_COLOR = 0x40FF40;
    
    @Inject(method = "getAverageWaterColor", at = @At("RETURN"), cancellable = true)
    private static void makeRainGreen(BlockAndTintGetter level, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        // Check if acid rain is active (raining and thundering as proxy)
        // Note: This will affect all water rendering, including lakes and rivers
        // This is intentional to create a cohesive acid rain atmosphere
        ClientLevel clientLevel = Minecraft.getInstance().level;
        if (clientLevel != null && clientLevel.isRaining() && clientLevel.isThundering()) {
            // Override the water color with green for acid rain effect
            cir.setReturnValue(ACID_RAIN_COLOR);
        }
    }
}
