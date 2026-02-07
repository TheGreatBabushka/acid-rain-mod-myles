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
}
