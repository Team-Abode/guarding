package com.teamabode.guarding.common.enchantment;

import net.minecraft.world.item.enchantment.Enchantment;

public class BarbedEnchantment extends GuardingEnchantment {

    public BarbedEnchantment() {
        super(Rarity.RARE);
    }

    public int getMinCost(int level) {
        return 20;
    }

    public int getMaxCost(int level) {
        return 50;
    }

    protected boolean checkCompatibility(Enchantment other) {
        return !(other instanceof RetributionEnchantment);
    }
}
