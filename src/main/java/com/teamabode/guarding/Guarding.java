package com.teamabode.guarding;

import com.chocohead.mm.api.ClassTinkerers;
import com.teamabode.guarding.core.registry.GuardingCallbacks;
import com.teamabode.guarding.core.registry.GuardingEnchantments;
import com.teamabode.guarding.core.registry.GuardingParticles;
import com.teamabode.guarding.core.registry.GuardingSounds;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Guarding implements ModInitializer {
    public static final String MOD_ID = "guarding";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final EnchantmentCategory GUARDING_SHIELD = ClassTinkerers.getEnum(EnchantmentCategory.class, "GUARDING_SHIELD");

    public void onInitialize() {
        LOGGER.info("Welcome to Guarding!");
        GuardingEnchantments.init();
        GuardingSounds.init();
        GuardingParticles.init();
        GuardingCallbacks.init();
    }
}
