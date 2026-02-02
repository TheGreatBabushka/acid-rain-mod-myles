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
    
    @Inject(method = "getAverageWaterColor", at = @At("RETURN"), cancellable = true)
    private static void makeRainGreen(BlockAndTintGetter level, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        // Check if acid rain is active (raining and thundering as proxy)
        ClientLevel clientLevel = Minecraft.getInstance().level;
        if (clientLevel != null && clientLevel.isRaining() && clientLevel.isThundering()) {
            // Override the water color with green for acid rain effect
            // 0x40FF40 is a greenish color
            cir.setReturnValue(0x40FF40);
        }
    }
}
