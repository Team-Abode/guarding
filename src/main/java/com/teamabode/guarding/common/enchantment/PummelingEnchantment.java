package com.teamabode.guarding.common.enchantment;

public class PummelingEnchantment extends GuardingEnchantment {

    public PummelingEnchantment() {
        super(Rarity.COMMON);
    }

    public int getMinCost(int level) {
        return level * 10;
    }

    public int getMaxCost(int level) {
        return this.getMinCost(level) + 30;
    }

    public int getMaxLevel() {
        return 3;
    }
}
