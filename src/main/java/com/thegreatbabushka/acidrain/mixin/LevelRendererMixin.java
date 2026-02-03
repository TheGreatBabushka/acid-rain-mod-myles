package com.thegreatbabushka.acidrain.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
     * Modify the red component of rain/snow particles.
     * During thunderstorms (acid rain), only rain is rendered, not snow.
     */
    @ModifyArg(
        method = "renderSnowAndRain",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;color(FFFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
        ),
        index = 0
    )
    private float modifyRainRed(float red) {
        return acidRainActiveCache ? ACID_RAIN_RED : red;
    }
    
    /**
     * Modify the green component of rain/snow particles.
     */
    @ModifyArg(
        method = "renderSnowAndRain",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;color(FFFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
        ),
        index = 1
    )
    private float modifyRainGreen(float green) {
        return acidRainActiveCache ? ACID_RAIN_GREEN : green;
    }
    
    /**
     * Modify the blue component of rain/snow particles.
     */
    @ModifyArg(
        method = "renderSnowAndRain",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;color(FFFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
        ),
        index = 2
    )
    private float modifyRainBlue(float blue) {
        return acidRainActiveCache ? ACID_RAIN_BLUE : blue;
    }
}
