package com.teamabode.guarding.core.registry;

import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.common.critieria.KilledByParriedArrowCriterion;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class GuardingCriterions {

    public static final KilledByParriedArrowCriterion KILLED_BY_PARRIED_ARROW = register("killed_by_parried_arrow", new KilledByParriedArrowCriterion());

    private static <T extends Criterion<?>> T register(String name, T trigger) {
        return Registry.register(Registries.CRITERION, Guarding.id(name), trigger);
    }

    public static void init() {

    }
}
