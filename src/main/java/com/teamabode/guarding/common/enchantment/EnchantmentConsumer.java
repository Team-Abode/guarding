package com.teamabode.guarding.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.entry.RegistryEntry;

@FunctionalInterface
public interface EnchantmentConsumer {
    void accept(RegistryEntry<Enchantment> holder, int level);
}
