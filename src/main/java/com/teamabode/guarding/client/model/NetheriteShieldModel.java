package com.teamabode.guarding.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabode.guarding.Guarding;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.armortrim.ArmorTrim;

public class NetheriteShieldModel extends ShieldModel {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(Guarding.id("netherite_shield"), "main");
    public static final ResourceLocation TEXTURE = Guarding.id("textures/entity/netherite_shield.png");

    private final TextureAtlas atlas;

    public NetheriteShieldModel(ModelPart modelPart, TextureAtlas atlas) {
        super(modelPart);
        this.atlas = atlas;
    }

    public void renderTrim(PoseStack poseStack, MultiBufferSource bufferSource, int light, ArmorTrim trim) {
        TextureAtlasSprite sprite = atlas.getSprite(trimTexture(trim));

        VertexConsumer vertex = sprite.wrap(bufferSource.getBuffer(RenderType.entityCutout(Sheets.ARMOR_TRIMS_SHEET)));
        this.renderToBuffer(poseStack, vertex, light, OverlayTexture.NO_OVERLAY);
    }

    public void renderGlint(PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        this.renderToBuffer(poseStack, ItemRenderer.getFoilBuffer(bufferSource, RenderType.armorEntityGlint(), false, true), light, OverlayTexture.NO_OVERLAY);
    }

    private static ResourceLocation trimTexture(ArmorTrim trim) {
        ResourceLocation patternLocation = trim.pattern().value().assetId();
        String material = trim.material().value().assetName();
        String colorMaterial = material.equals("netherite") ? "netherite_darker" : material;
        return patternLocation.withPath(path -> "trims/shield/" + path + "_" + colorMaterial);
    }
}
