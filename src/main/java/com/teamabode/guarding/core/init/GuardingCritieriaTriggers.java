package com.teamabode.guarding.core.init;

import com.teamabode.guarding.common.critieria.KilledByParriedArrowTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class GuardingCritieriaTriggers {

    public static final KilledByParriedArrowTrigger KILLED_BY_PARRIED_ARROW = CriteriaTriggers.register(new KilledByParriedArrowTrigger());

    public static void init() {

    }
}
