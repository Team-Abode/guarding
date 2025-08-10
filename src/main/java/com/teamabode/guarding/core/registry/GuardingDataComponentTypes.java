package com.teamabode.guarding.core.registry;

import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.common.component.ParriesAttacksComponent;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.function.UnaryOperator;

public class GuardingDataComponentTypes {

    public static final ComponentType<ParriesAttacksComponent> PARRIES_ATTACKS = register(
            "parries_attacks", builder -> builder
                    .codec(ParriesAttacksComponent.CODEC)
                    .packetCodec(ParriesAttacksComponent.PACKET_CODEC)
    );

    private static <T> ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builder) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Guarding.id(name), builder.apply(ComponentType.builder()).build());
    }

    public static void init() {}
}
