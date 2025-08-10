package com.teamabode.guarding.core.registry;

import com.teamabode.guarding.Guarding;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;

public class GuardingSounds {

    public static final RegistryEntry<SoundEvent> ITEM_SHIELD_PARRY = register("item.shield.parry");

    public static final RegistryEntry<SoundEvent> ITEM_NETHERITE_SHIELD_BLOCK = register("item.netherite_shield.block");
    public static final RegistryEntry<SoundEvent> ITEM_NETHERITE_SHIELD_BREAK = register("item.netherite_shield.break");
    public static final RegistryEntry<SoundEvent> ITEM_NETHERITE_SHIELD_EQUIP = register("item.netherite_shield.equip");

    private static RegistryEntry.Reference<SoundEvent> register(String name) {
        return Registry.registerReference(Registries.SOUND_EVENT, Guarding.id(name), SoundEvent.of(Guarding.id(name)));
    }

    public static void init() {

    }
}
