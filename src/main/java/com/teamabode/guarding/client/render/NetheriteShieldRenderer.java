package com.teamabode.guarding.client.render;

import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.client.model.NetheriteShieldModel;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import java.util.Optional;

public class NetheriteShieldRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer, SimpleSynchronousResourceReloadListener {
    private NetheriteShieldModel model;

    public Identifier getFabricId() {
        return Guarding.id("netherite_shield_renderer");
    }

    public void render(ItemStack stack, ModelTransformationMode displayContext, MatrixStack poseStack, VertexConsumerProvider bufferSource, int light, int overlay) {
        poseStack.push();
        poseStack.scale(1.0f, -1.0f, -1.0f);

        VertexConsumer buffer = bufferSource.getBuffer(model.getLayer(NetheriteShieldModel.TEXTURE));
        model.render(poseStack, buffer, light, overlay);

        if (stack.contains(DataComponentTypes.TRIM)) {
            model.renderTrim(poseStack, bufferSource, light, stack.get(DataComponentTypes.TRIM));
        }
        if (stack.hasGlint()) {
            model.renderGlint(poseStack, bufferSource, light);
        }
        poseStack.pop();
    }

    public void reload(ResourceManager manager) {
        MinecraftClient instance = MinecraftClient.getInstance();
        this.model = new NetheriteShieldModel(instance.getEntityModelLoader().getModelPart(NetheriteShieldModel.LAYER), instance.getBakedModelManager().getAtlas(TexturedRenderLayers.ARMOR_TRIMS_ATLAS_TEXTURE));
    }
}
