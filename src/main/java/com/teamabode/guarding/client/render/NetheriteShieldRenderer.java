package com.teamabode.guarding.client.render;

import com.mojang.serialization.MapCodec;
import com.teamabode.guarding.Guarding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.entity.model.ShieldEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.item.model.special.ShieldModelRenderer;
import net.minecraft.client.render.item.model.special.SpecialModelRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.item.equipment.trim.ArmorTrim;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Set;

public class NetheriteShieldRenderer extends ShieldModelRenderer {
    public static final Identifier TEXTURE_ID = Guarding.id("textures/entity/netherite_shield.png");

    private final ShieldEntityModel model;
    private final SpriteAtlasTexture armorTrimAtlas;

    public NetheriteShieldRenderer(ShieldEntityModel model, SpriteAtlasTexture armorTrimAtlas) {
        super(model);
        this.model = model;
        this.armorTrimAtlas = armorTrimAtlas;
    }

    @Override
    public void render(@Nullable ComponentMap componentMap, ItemDisplayContext itemDisplayContext, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay, boolean glint) {
        matrixStack.push();

        matrixStack.scale(1.0f, -1.0f, -1.0f);


        VertexConsumer vertices = vertexConsumerProvider.getBuffer(model.getLayer(TEXTURE_ID));

        this.model.render(matrixStack, vertices, light, overlay);

        if (componentMap != null && componentMap.contains(DataComponentTypes.TRIM)) {
            this.renderTrims(componentMap.get(DataComponentTypes.TRIM), matrixStack, vertexConsumerProvider, light);
        }

        if (glint) {
            this.renderGlint(matrixStack, vertexConsumerProvider, itemDisplayContext == ItemDisplayContext.GUI, light);
        }

        matrixStack.pop();
    }

    @Override
    public void collectVertices(Set<Vector3f> vertices) {
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.scale(1.0f, -1.0f, -1.0f);
        this.model.getRootPart().collectVertices(matrixStack, vertices);
    }

    private void renderTrims(ArmorTrim trim, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        Sprite sprite = armorTrimAtlas.getSprite(this.getTrimTexture(trim));

        VertexConsumer vertices = sprite.getTextureSpecificVertexConsumer(
                vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(TexturedRenderLayers.ARMOR_TRIMS_ATLAS_TEXTURE))
        );

        this.model.render(matrixStack, vertices, light, OverlayTexture.DEFAULT_UV);
    }

    private void renderGlint(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, boolean solid, int light) {
        this.model.render(matrixStack, ItemRenderer.getItemGlintConsumer(
                vertexConsumerProvider,
                RenderLayer.getArmorEntityGlint(),
                solid,
                true
        ), light, OverlayTexture.DEFAULT_UV);
    }

    private Identifier getTrimTexture(ArmorTrim trim) {
        Identifier patternId = trim.pattern().value().assetId();
        String material = trim.material()
                .value()
                .assets()
                .getAssetId(EquipmentAssetKeys.NETHERITE)
                .suffix();

        return patternId.withPath(path -> "trims/shield/" + path + "_" + material);
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final Unbaked INSTANCE = new Unbaked();
        public static final MapCodec<Unbaked> CODEC = MapCodec.unit(INSTANCE);

        @Override
        public @Nullable SpecialModelRenderer<?> bake(LoadedEntityModels entityModels) {
            return new NetheriteShieldRenderer(
                    new ShieldEntityModel(entityModels.getModelPart(EntityModelLayers.SHIELD)),
                    MinecraftClient.getInstance().getBakedModelManager().getAtlas(TexturedRenderLayers.ARMOR_TRIMS_ATLAS_TEXTURE)
            );
        }

        @Override
        public MapCodec<? extends SpecialModelRenderer.Unbaked> getCodec() {
            return CODEC;
        }
    }
}
