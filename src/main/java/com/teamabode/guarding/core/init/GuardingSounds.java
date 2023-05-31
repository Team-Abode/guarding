package com.teamabode.guarding.core.init;

import com.teamabode.guarding.Guarding;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class GuardingSounds {

    public static final SoundEvent ITEM_SHIELD_PARRY = register("item.shield.parry");

    // Netherite Shield sounds
    public static final SoundEvent ITEM_NETHERITE_SHIELD_BLOCK = register("item.netherite_shield.block");
    public static final SoundEvent ITEM_NETHERITE_SHIELD_BREAK = register("item.netherite_shield.break");
    public static final SoundEvent ITEM_NETHERITE_SHIELD_PARRY = register("item.netherite_shield.parry");
    public static final SoundEvent ITEM_NETHERITE_SHIELD_EQUIP = register("item.netherite_shield.equip");

    private static SoundEvent register(String name) {
        var resourceLocation = new ResourceLocation(Guarding.MOD_ID, name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, resourceLocation, SoundEvent.createVariableRangeEvent(resourceLocation));
    }

    public static void init() {

    }
}
