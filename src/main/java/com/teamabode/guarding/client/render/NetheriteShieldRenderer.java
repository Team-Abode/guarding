package com.teamabode.guarding.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.client.model.NetheriteShieldModel;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;

import java.util.Optional;

public class NetheriteShieldRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer, SimpleSynchronousResourceReloadListener {
    private NetheriteShieldModel model;

    public ResourceLocation getFabricId() {
        return new ResourceLocation(Guarding.MOD_ID, "netherite_shield_renderer");
    }

    public void render(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        poseStack.pushPose();
        poseStack.scale(1.0f, -1.0f, -1.0f);

        VertexConsumer buffer = bufferSource.getBuffer(model.renderType(NetheriteShieldModel.TEXTURE));
        model.renderToBuffer(poseStack, buffer, light, overlay, 1.0f, 1.0f, 1.0f, 1.0f);

        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            ArmorTrim.getTrim(level.registryAccess(), stack).ifPresent(armorTrim -> {
                model.renderTrim(poseStack, bufferSource, light, armorTrim);
            });
        }
        if (stack.hasFoil()) {
            model.renderGlint(poseStack, bufferSource, light);
        }
        poseStack.popPose();
    }

    public void onResourceManagerReload(ResourceManager manager) {
        this.model = new NetheriteShieldModel(Minecraft.getInstance().getEntityModels().bakeLayer(NetheriteShieldModel.LAYER), Minecraft.getInstance().getModelManager().getAtlas(Sheets.ARMOR_TRIMS_SHEET));
    }
}
