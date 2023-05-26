package com.teamabode.guarding.core.registry;

import com.teamabode.guarding.Guarding;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class GuardingParticles {

    public static final SimpleParticleType PARRY = register("parry");

    private static SimpleParticleType register(String name) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, new ResourceLocation(Guarding.MOD_ID, name), FabricParticleTypes.simple(true));
    }

    public static void init() {

    }
}
