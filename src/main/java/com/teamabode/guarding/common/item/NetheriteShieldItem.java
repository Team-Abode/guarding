package com.teamabode.guarding.common.item;

import com.teamabode.guarding.Guarding;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.ShieldItem;

public class NetheriteShieldItem extends ShieldItem {

    public NetheriteShieldItem(Properties properties) {
        super(properties);
    }

    public int getEnchantmentValue() {
        return 15;
    }

    public boolean isEnabled(FeatureFlagSet enabledFeatures) {
        return Guarding.CONFIG.getGroup("general").getBooleanProperty("netherite_shield_enabled");
    }
}
