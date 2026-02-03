package com.thegreatbabushka.acidrain.mixin;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Mixin to change rain particle colors from blue to green during acid rain
 */
@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    
    // Greenish color components for acid rain effect
    private static final float ACID_RAIN_RED = 0.25f;    // 64/255
    private static final float ACID_RAIN_GREEN = 1.0f;   // 255/255
    private static final float ACID_RAIN_BLUE = 0.25f;   // 64/255
    
    /**
     * Check if acid rain is currently active
     */
    private static boolean isAcidRainActive() {
        ClientLevel level = Minecraft.getInstance().level;
        return level != null && level.isRaining() && level.isThundering();
    }
    
    /**
     * Redirect the color() call to use green colors during acid rain
     */
    @Redirect(
        method = "renderSnowAndRain",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;color(FFFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
        )
    )
    private VertexConsumer redirectRainColor(VertexConsumer instance, float red, float green, float blue, float alpha) {
        if (isAcidRainActive()) {
            return instance.color(ACID_RAIN_RED, ACID_RAIN_GREEN, ACID_RAIN_BLUE, alpha);
        }
        return instance.color(red, green, blue, alpha);
    }
}
