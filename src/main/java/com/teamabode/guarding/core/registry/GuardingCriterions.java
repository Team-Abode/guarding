package com.teamabode.guarding.core.registry;

import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.common.critieria.KilledByParriedArrowCriterion;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class GuardingCriterions {

    public static final KilledByParriedArrowCriterion KILLED_BY_PARRIED_ARROW = register("killed_by_parried_arrow", new KilledByParriedArrowCriterion());

    private static <T extends CriterionTrigger<?>> T register(String name, T trigger) {
        return Registry.register(BuiltInRegistries.TRIGGER_TYPES, Guarding.id(name), trigger);
    }

    public static void init() {

    }
}
