package com.thegreatbabushka.acidrain.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class AcidRainRenderer {
    
    private static final ResourceLocation RAIN_TEXTURE = new ResourceLocation("textures/environment/rain.png");
    
    @SubscribeEvent
    public static void onFogColor(ViewportEvent.ComputeFogColor event) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null || !level.isRaining()) {
            return;
        }

        // Check if acid rain is active
        // Note: On client side, we need to sync this from server
        // For now, we'll apply green tint when it's raining and thundering
        // This will need to be synced with server state in a full implementation
        if (isClientAcidRainActive()) {
            // Add green tint to fog color when acid rain is active
            float greenModifier = 0.3f;
            event.setRed(event.getRed() * 0.7f);
            event.setGreen(event.getGreen() * 1.1f + greenModifier);
            event.setBlue(event.getBlue() * 0.7f);
        }
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        // Render custom acid rain during the weather rendering stage
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_WEATHER) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        
        // Only render when acid rain is active
        if (level == null || !level.isRaining() || !isClientAcidRainActive()) {
            return;
        }

        // Render green-tinted rain particles
        renderAcidRainParticles(event, mc, level);
    }

    private static void renderAcidRainParticles(RenderLevelStageEvent event, Minecraft mc, ClientLevel level) {
        float rainStrength = level.getRainLevel(event.getPartialTick());
        if (rainStrength <= 0.0F) {
            return;
        }

        LightTexture lightTexture = mc.gameRenderer.lightTexture();
        lightTexture.turnOnLightLayer();

        int playerX = Mth.floor(mc.player.getX());
        int playerY = Mth.floor(mc.player.getY());
        int playerZ = Mth.floor(mc.player.getZ());
        
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(Minecraft.useShaderTransparency());
        RenderSystem.setShader(GameRenderer::getParticleShader);
        RenderSystem.setShaderTexture(0, RAIN_TEXTURE);
        
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        
        int renderDistance = 10; // Render rain within 10 blocks
        
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        Matrix4f matrix = event.getPoseStack().last().pose();
        
        for (int z = playerZ - renderDistance; z <= playerZ + renderDistance; ++z) {
            for (int x = playerX - renderDistance; x <= playerX + renderDistance; ++x) {
                int heightAtPos = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
                int minY = playerY - renderDistance;
                int maxY = playerY + renderDistance;
                int rainStartY = Math.max(heightAtPos, minY);
                
                // Clamp minY and maxY to be at or above the rain start height
                if (minY < rainStartY) {
                    minY = rainStartY;
                }
                if (maxY < rainStartY) {
                    maxY = rainStartY;
                }
                
                if (minY != maxY) {
                    RandomSource random = RandomSource.create((long)(x * x * 3121 + x * 45238971 ^ z * z * 418711 + z * 13761));
                    mutablePos.set(x, rainStartY, z);
                    
                    Biome biome = level.getBiome(mutablePos).value();
                    if (biome.getPrecipitationAt(mutablePos) == Biome.Precipitation.RAIN) {
                        double xOffset = (double)x - mc.player.getX();
                        double zOffset = (double)z - mc.player.getZ();
                        float distanceFromPlayer = (float)Math.sqrt(xOffset * xOffset + zOffset * zOffset) / (float)renderDistance;
                        
                        if (distanceFromPlayer < 1.0F) {
                            mutablePos.set(x, rainStartY, z);
                            int packedLight = LightTexture.pack(level.getBrightness(LightTexture.BLOCK_SKY, mutablePos), 
                                                                level.getBrightness(LightTexture.BLOCK_BLOCK, mutablePos));
                            
                            float particleX = (float)((double)x + 0.5D - mc.player.getX());
                            float particleZ = (float)((double)z + 0.5D - mc.player.getZ());
                            
                            // Calculate alpha based on distance
                            float alpha = ((1.0F - distanceFromPlayer * distanceFromPlayer) * 0.5F + 0.5F) * rainStrength;
                            
                            // Green tint for acid rain - emphasize green channel
                            float red = 0.5F;
                            float green = 1.0F;
                            float blue = 0.5F;
                            
                            // Render multiple rain streaks
                            for (int streak = 0; streak < 2; streak++) {
                                float yOffset = (float)(minY + random.nextInt(maxY - minY + 1)) - (float)mc.player.getY();
                                
                                if (yOffset >= -8.0F && yOffset < 8.0F) {
                                    float u0 = (float)streak * 0.5F;
                                    float v0 = 0.0F;
                                    float u1 = u0 + 0.5F;
                                    float v1 = 1.0F;
                                    
                                    float width = 0.1F;
                                    float length = 0.5F;
                                    
                                    // Draw rain drop as a quad
                                    buffer.vertex(matrix, particleX - width, yOffset, particleZ - width)
                                          .uv(u1, v1)
                                          .color(red, green, blue, alpha)
                                          .uv2(packedLight)
                                          .endVertex();
                                    buffer.vertex(matrix, particleX + width, yOffset, particleZ + width)
                                          .uv(u0, v1)
                                          .color(red, green, blue, alpha)
                                          .uv2(packedLight)
                                          .endVertex();
                                    buffer.vertex(matrix, particleX + width, yOffset + length, particleZ + width)
                                          .uv(u0, v0)
                                          .color(red, green, blue, alpha)
                                          .uv2(packedLight)
                                          .endVertex();
                                    buffer.vertex(matrix, particleX - width, yOffset + length, particleZ - width)
                                          .uv(u1, v0)
                                          .color(red, green, blue, alpha)
                                          .uv2(packedLight)
                                          .endVertex();
                                }
                            }
                        }
                    }
                }
            }
        }
        
        tesselator.end();
        
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        lightTexture.turnOffLightLayer();
    }

    // This is a placeholder - in a full implementation, this should be synced from server
    private static boolean isClientAcidRainActive() {
        // For now, we'll check if it's both raining and thundering as a proxy
        ClientLevel level = Minecraft.getInstance().level;
        return level != null && level.isRaining() && level.isThundering();
    }
}
