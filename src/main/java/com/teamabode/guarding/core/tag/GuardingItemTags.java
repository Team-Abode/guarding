package com.teamabode.guarding.core.tag;

import com.teamabode.guarding.Guarding;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class GuardingItemTags {

    public static final TagKey<Item> SHIELD_ENCHANTABLE = TagKey.create(Registries.ITEM, Guarding.id("enchantable/shield"));
}
