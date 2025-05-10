package com.teamabode.guarding.core.registry;

import com.teamabode.guarding.Guarding;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public class GuardingStats {
    public static final ResourceLocation ATTACKS_BLOCKED_BY_SHIELD = register("attacks_blocked_by_shield", StatFormatter.DEFAULT);
    public static final ResourceLocation ATTACKS_PARRIED_BY_SHIELD = register("attacks_parried_by_shield", StatFormatter.DEFAULT);

    private static ResourceLocation register(String name, StatFormatter format) {
        ResourceLocation location = Guarding.id(name);
        Registry.register(BuiltInRegistries.CUSTOM_STAT, location, location);
        Stats.CUSTOM.get(location, format);
        return location;
    }

    public static void init() {}
}
