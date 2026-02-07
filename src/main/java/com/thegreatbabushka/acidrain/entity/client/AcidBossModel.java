package com.thegreatbabushka.acidrain.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.thegreatbabushka.acidrain.entity.custom.AcidBossEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class AcidBossModel extends EntityModel<AcidBossEntity> {
    private final ModelPart body;
    private final ModelPart mouth;
    private final ModelPart upperTeeth;
    private final ModelPart lowerTeeth;

    public AcidBossModel(ModelPart root) {
        this.body = root.getChild("body");
        this.mouth = root.getChild("mouth");
        this.upperTeeth = this.mouth.getChild("upper_teeth");
        this.lowerTeeth = this.mouth.getChild("lower_teeth");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        // Main slime body - positioned at entity feet
        PartDefinition body = partdefinition.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-8.0F, 0.0F, -8.0F, 16.0F, 16.0F, 16.0F),
                PartPose.offset(0.0F, 8.0F, 0.0F));

        // Mouth area - attached to body front, opens forward
        PartDefinition mouth = body.addOrReplaceChild("mouth",
                CubeListBuilder.create()
                        .texOffs(0, 32)
                        .addBox(-6.0F, -1.0F, -8.0F, 12.0F, 6.0F, 2.0F),
                PartPose.offset(0.0F, 4.0F, -8.0F));

        // Upper teeth
        PartDefinition upperTeeth = mouth.addOrReplaceChild("upper_teeth",
                CubeListBuilder.create()
                        .texOffs(48, 0)
                        .addBox(-5.0F, 0.0F, -1.0F, 10.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, -1.0F, -7.0F));

        // Lower teeth
        PartDefinition lowerTeeth = mouth.addOrReplaceChild("lower_teeth",
                CubeListBuilder.create()
                        .texOffs(48, 2)
                        .addBox(-5.0F, -1.0F, -1.0F, 10.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 5.0F, -7.0F));

        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    @Override
    public void setupAnim(AcidBossEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // Slime-like squashing animation
        float squash = Mth.sin(ageInTicks * 0.3F) * 0.1F;
        this.body.xScale = 1.0F + squash;
        this.body.yScale = 1.0F - squash;
        this.body.zScale = 1.0F + squash;

        // Mouth opening animation - rotate around pivot point
        if (entity.isMouthOpen()) {
            // Open mouth by rotating outward
            this.mouth.xRot = -0.3F;
        } else {
            this.mouth.xRot = 0.0F;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        mouth.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
