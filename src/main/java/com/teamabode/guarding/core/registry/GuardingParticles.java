package com.teamabode.guarding.core.registry;

import com.teamabode.guarding.Guarding;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class GuardingParticles {

    public static final SimpleParticleType PARRY = register("parry");

    private static SimpleParticleType register(String name) {
        return Registry.register(Registries.PARTICLE_TYPE, Guarding.id(name), FabricParticleTypes.simple(true));
    }

    public static void init() {

    }
}
