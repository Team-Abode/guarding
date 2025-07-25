package com.teamabode.guarding.client.model;

import com.teamabode.guarding.Guarding;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.ShieldEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.util.Identifier;

public class NetheriteShieldModel extends ShieldEntityModel {
    public static final EntityModelLayer LAYER = new EntityModelLayer(Guarding.id("netherite_shield"), "main");
    public static final Identifier TEXTURE = Guarding.id("textures/entity/netherite_shield.png");

    private final SpriteAtlasTexture atlas;

    public NetheriteShieldModel(ModelPart modelPart, SpriteAtlasTexture atlas) {
        super(modelPart);
        this.atlas = atlas;
    }

    public void renderTrim(MatrixStack poseStack, VertexConsumerProvider bufferSource, int light, ArmorTrim trim) {
        Sprite sprite = atlas.getSprite(trimTexture(trim));

        VertexConsumer vertex = sprite.getTextureSpecificVertexConsumer(bufferSource.getBuffer(RenderLayer.getEntityCutout(TexturedRenderLayers.ARMOR_TRIMS_ATLAS_TEXTURE)));
        this.render(poseStack, vertex, light, OverlayTexture.DEFAULT_UV);
    }

    public void renderGlint(MatrixStack poseStack, VertexConsumerProvider bufferSource, int light) {
        this.render(poseStack, ItemRenderer.getItemGlintConsumer(bufferSource, RenderLayer.getArmorEntityGlint(), false, true), light, OverlayTexture.DEFAULT_UV);
    }

    private static Identifier trimTexture(ArmorTrim trim) {
        Identifier patternLocation = trim.getPattern().value().assetId();
        String material = trim.getMaterial().value().assetName();
        String colorMaterial = material.equals("netherite") ? "netherite_darker" : material;
        return patternLocation.withPath(path -> "trims/shield/" + path + "_" + colorMaterial);
    }
}
