package com.teamabode.guarding.core.registry;

import com.teamabode.guarding.Guarding;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

public class GuardingSounds {

    public static final SoundEvent ITEM_SHIELD_PARRY = register("item.shield.parry");

    public static final SoundEvent ITEM_NETHERITE_SHIELD_BLOCK = register("item.netherite_shield.block");
    public static final SoundEvent ITEM_NETHERITE_SHIELD_BREAK = register("item.netherite_shield.break");
    public static final Holder<SoundEvent> ITEM_NETHERITE_SHIELD_EQUIP = registerHolder("item.netherite_shield.equip");

    private static SoundEvent register(String name) {
        return Registry.register(BuiltInRegistries.SOUND_EVENT, Guarding.id(name), SoundEvent.createVariableRangeEvent(Guarding.id(name)));
    }

    private static Holder.Reference<SoundEvent> registerHolder(String name) {
        return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, Guarding.id(name), SoundEvent.createVariableRangeEvent(Guarding.id(name)));
    }

    public static void init() {

    }
}
