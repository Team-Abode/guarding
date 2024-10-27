package com.teamabode.guarding.core.registry;

import com.teamabode.guarding.Guarding;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;

public class GuardingStats {
    public static final Identifier ATTACKS_BLOCKED_BY_SHIELD = register("attacks_blocked_by_shield", StatFormatter.DEFAULT);
    public static final Identifier ATTACKS_PARRIED_BY_SHIELD = register("attacks_parried_by_shield", StatFormatter.DEFAULT);

    private static Identifier register(String name, StatFormatter format) {
        Identifier location = Guarding.id(name);
        Registry.register(Registries.CUSTOM_STAT, location, location);
        Stats.CUSTOM.getOrCreateStat(location, format);
        return location;
    }

    public static void init() {}
}
