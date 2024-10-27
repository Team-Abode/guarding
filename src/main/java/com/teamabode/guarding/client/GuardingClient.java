package com.teamabode.guarding.client;

import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.client.model.NetheriteShieldModel;
import com.teamabode.guarding.client.particle.ParryParticle;
import com.teamabode.guarding.client.render.NetheriteShieldRenderer;
import com.teamabode.guarding.core.registry.GuardingItems;
import com.teamabode.guarding.core.registry.GuardingParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceType;

public class GuardingClient implements ClientModInitializer {
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(GuardingParticles.PARRY, ParryParticle.Provider::new);
        EntityModelLayerRegistry.registerModelLayer(NetheriteShieldModel.LAYER, NetheriteShieldModel::getTexturedModelData);
        netheriteShield();
    }

    private static void netheriteShield() {
        NetheriteShieldRenderer renderer = new NetheriteShieldRenderer();
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(renderer);
        BuiltinItemRendererRegistry.INSTANCE.register(GuardingItems.NETHERITE_SHIELD, renderer);
        ModelPredicateProviderRegistry.register(GuardingItems.NETHERITE_SHIELD, Guarding.id("blocking"), GuardingClient::blockingPredicate);
    }

    private static float blockingPredicate(ItemStack stack, ClientWorld level, LivingEntity user, int i) {
        return user != null && user.getActiveItem() == stack ? 1.0f : 0.0f;
    }
}
