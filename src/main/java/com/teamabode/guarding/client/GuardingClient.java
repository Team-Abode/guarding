package com.teamabode.guarding.client;

import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.client.model.NetheriteShieldModel;
import com.teamabode.guarding.client.particle.ParryParticle;
import com.teamabode.guarding.client.render.NetheriteShieldRenderer;
import com.teamabode.guarding.core.init.GuardingItems;
import com.teamabode.guarding.core.init.GuardingParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class GuardingClient implements ClientModInitializer {

    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(GuardingParticles.PARRY, ParryParticle.Provider::new);
        EntityModelLayerRegistry.registerModelLayer(NetheriteShieldModel.LAYER, NetheriteShieldModel::createLayer);

        renderNetheriteShield();
    }

    private static void renderNetheriteShield() {
        NetheriteShieldRenderer renderer = new NetheriteShieldRenderer();
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(renderer);
        BuiltinItemRendererRegistry.INSTANCE.register(GuardingItems.NETHERITE_SHIELD, renderer);
        ItemProperties.register(GuardingItems.NETHERITE_SHIELD, new ResourceLocation(Guarding.MOD_ID,"blocking"), GuardingClient::blockingPredicate);
    }

    private static float blockingPredicate(ItemStack stack, ClientLevel level, LivingEntity user, int i) {
        return user != null && user.getUseItem() == stack ? 1.0f : 0.0f;
    }
}
