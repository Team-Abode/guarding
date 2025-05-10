package com.teamabode.guarding.common.enchantment;

import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;

@FunctionalInterface
public interface EnchantmentConsumer {
    void accept(Holder<Enchantment> holder, int level);
}
