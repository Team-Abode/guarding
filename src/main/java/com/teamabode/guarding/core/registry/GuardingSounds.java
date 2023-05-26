package com.teamabode.guarding.core.registry;

import com.teamabode.guarding.Guarding;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class GuardingSounds {

    public static final SoundEvent ITEM_SHIELD_PARRY = register("item.shield.parry");

    private static SoundEvent register(String name) {
        var resourceLocation = new ResourceLocation(Guarding.MOD_ID, name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, resourceLocation, SoundEvent.createVariableRangeEvent(resourceLocation));
    }

    public static void init() {

    }
}
