package com.teamabode.guarding.client;

import com.teamabode.guarding.client.particle.ParryParticle;
import com.teamabode.guarding.core.registry.GuardingParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class GuardingClient implements ClientModInitializer {

    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(GuardingParticles.PARRY, ParryParticle.Provider::new);
    }
}
