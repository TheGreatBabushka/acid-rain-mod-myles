package com.thegreatbabushka.acidrain.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

/**
 * Mixin to change rain particle colors from blue to green during acid rain
 */
@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    
    // Greenish color components for acid rain effect
    private static final float ACID_RAIN_RED = 0.25f;    // 64/255
    private static final float ACID_RAIN_GREEN = 1.0f;   // 255/255
    private static final float ACID_RAIN_BLUE = 0.25f;   // 64/255
    
    @Unique
    private boolean acidRainActiveCache = false;
    
    /**
     * Cache the acid rain state at the start of rendering to avoid repeated checks
     */
    @Inject(method = "renderSnowAndRain", at = @At("HEAD"))
    private void cacheAcidRainState(CallbackInfo ci) {
        ClientLevel level = Minecraft.getInstance().level;
        acidRainActiveCache = level != null && level.isRaining() && level.isThundering();
    }
    
    /**
     * Modify all color calls in rain rendering to change from blue to green.
     * Note: This affects all particles in renderSnowAndRain, but during thunderstorms
     * (our acid rain condition), only rain particles are rendered, not snow.
     */
    @ModifyArgs(
        method = "renderSnowAndRain",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;color(FFFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
        )
    )
    private void modifyRainColor(Args args) {
        if (acidRainActiveCache) {
            args.set(0, ACID_RAIN_RED);    // Red component
            args.set(1, ACID_RAIN_GREEN);  // Green component
            args.set(2, ACID_RAIN_BLUE);   // Blue component
            // Keep alpha (index 3) as is
        }
    }
}
