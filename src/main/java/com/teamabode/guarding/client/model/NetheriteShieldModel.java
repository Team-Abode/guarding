package com.teamabode.guarding.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabode.guarding.Guarding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.armortrim.ArmorTrim;

import java.security.Guard;

public class NetheriteShieldModel extends ShieldModel {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(Guarding.MOD_ID, "netherite_shield"), "main");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Guarding.MOD_ID, "textures/entity/netherite_shield.png");

    public NetheriteShieldModel(ModelPart modelPart) {
        super(modelPart);
    }

    public void renderTrim(PoseStack poseStack, MultiBufferSource bufferSource, int light, ArmorTrim trim) {
        TextureAtlas atlas = Minecraft.getInstance().getModelManager().getAtlas(Sheets.ARMOR_TRIMS_SHEET);
        TextureAtlasSprite sprite = atlas.getSprite(trimTexture(trim));
        VertexConsumer buffer = bufferSource.getBuffer(Sheets.armorTrimsSheet());

        VertexConsumer vertex = sprite.wrap(buffer);

        this.renderToBuffer(poseStack, vertex, light, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void renderGlint(PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        this.renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.armorEntityGlint()), light, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
    }

    private static ResourceLocation trimTexture(ArmorTrim trim) {
        ResourceLocation patternLocation = trim.pattern().value().assetId();
        String material = trim.material().value().assetName();
        String colorMaterial = material.equals("netherite") ? "netherite_darker" : material;
        return patternLocation.withPath(path -> "trims/shield/" + path + "_" + colorMaterial);
    }
}
