package com.teamabode.guarding.core.api.asm;

import com.teamabode.guarding.core.mixin.gameplay.EnchantmentCategoryMixin;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShieldItem;

public class GuardingEnchantmentCategory extends EnchantmentCategoryMixin {

    public boolean canEnchant(Item item) {
        return item instanceof ShieldItem;
    }
}
