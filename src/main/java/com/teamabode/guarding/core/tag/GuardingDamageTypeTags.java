package com.teamabode.guarding.core.tag;

import com.teamabode.guarding.Guarding;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public class GuardingDamageTypeTags {

    public static final TagKey<DamageType> NO_PARRY = TagKey.create(Registries.DAMAGE_TYPE, Guarding.id("no_parry"));
}
