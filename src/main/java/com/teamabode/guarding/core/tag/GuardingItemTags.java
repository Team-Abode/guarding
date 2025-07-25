package com.teamabode.guarding.core.tag;

import com.teamabode.guarding.Guarding;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class GuardingItemTags {

    public static final TagKey<Item> SHIELD_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, Guarding.id("enchantable/shield"));
}
