package com.teamabode.guarding.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.client.model.NetheriteShieldModel;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import java.util.Optional;

public class NetheriteShieldRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer, SimpleSynchronousResourceReloadListener {
    private NetheriteShieldModel model;

    public ResourceLocation getFabricId() {
        return Guarding.id("netherite_shield_renderer");
    }

    public void render(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        poseStack.pushPose();
        poseStack.scale(1.0f, -1.0f, -1.0f);

        VertexConsumer buffer = bufferSource.getBuffer(model.renderType(NetheriteShieldModel.TEXTURE));
        model.renderToBuffer(poseStack, buffer, light, overlay);

        if (stack.has(DataComponents.TRIM)) {
            model.renderTrim(poseStack, bufferSource, light, stack.get(DataComponents.TRIM));
        }
        if (stack.hasFoil()) {
            model.renderGlint(poseStack, bufferSource, light);
        }
        poseStack.popPose();
    }

    public void onResourceManagerReload(ResourceManager manager) {
        Minecraft instance = Minecraft.getInstance();
        this.model = new NetheriteShieldModel(instance.getEntityModels().bakeLayer(NetheriteShieldModel.LAYER), instance.getModelManager().getAtlas(Sheets.ARMOR_TRIMS_SHEET));
    }
}
