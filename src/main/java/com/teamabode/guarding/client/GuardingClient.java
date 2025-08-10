package com.teamabode.guarding.client;

import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.client.particle.ParryParticle;
import com.teamabode.guarding.client.render.NetheriteShieldRenderer.Unbaked;
import com.teamabode.guarding.core.registry.GuardingParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.render.item.model.special.SpecialModelTypes;

public class GuardingClient implements ClientModInitializer {

    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(GuardingParticles.PARRY, ParryParticle.Provider::new);
        SpecialModelTypes.ID_MAPPER.put(Guarding.id("netherite_shield"), Unbaked.CODEC);

        // EntityModelLayerRegistry.registerModelLayer(NetheriteShieldModel.LAYER, NetheriteShieldModel::getTexturedModelData);
    }

}
