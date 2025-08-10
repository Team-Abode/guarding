package com.teamabode.guarding.core.tag;

import com.teamabode.guarding.Guarding;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class GuardingDamageTypeTags {
    public static final TagKey<DamageType> CANNOT_PARRY = TagKey.of(RegistryKeys.DAMAGE_TYPE, Guarding.id("cannot_parry"));
}
