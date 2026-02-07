package com.thegreatbabushka.acidrain.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.thegreatbabushka.acidrain.AcidRainMod;
import com.thegreatbabushka.acidrain.entity.custom.AcidBossEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class AcidBossRenderer extends MobRenderer<AcidBossEntity, AcidBossModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(AcidRainMod.MOD_ID, "textures/entity/acid_boss.png");

    public AcidBossRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new AcidBossModel(pContext.bakeLayer(ModModelLayers.ACID_BOSS_LAYER)), 1.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(AcidBossEntity pEntity) {
        return TEXTURE;
    }

    @Override
    public void render(AcidBossEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        // Scale based on size
        float scale = pEntity.getSize() * 0.5F;
        pMatrixStack.scale(scale, scale, scale);
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}
