package com.teamabode.guarding.common.enchantment;

import com.teamabode.guarding.Guarding;
import net.minecraft.world.item.enchantment.Enchantment;

public class RetributionEnchantment extends GuardingEnchantment {

    public RetributionEnchantment() {
        super(Rarity.RARE);
    }

    public int getMinCost(int level) {
        return level * 25;
    }

    public int getMaxCost(int level) {
        return 50;
    }

    public int getMaxLevel() {
        return 2;
    }

    public boolean isTreasureOnly() {
        return Guarding.CONFIG.getGroup("retribution").getBooleanProperty("is_treasure");
    }

    protected boolean checkCompatibility(Enchantment other) {
        return !(other instanceof BarbedEnchantment);
    }
}
