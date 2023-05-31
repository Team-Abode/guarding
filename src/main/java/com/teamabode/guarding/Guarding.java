package com.teamabode.guarding;

import com.chocohead.mm.api.ClassTinkerers;
import com.teamabode.guarding.core.init.*;
import com.teamabode.scribe.core.api.config.Config;
import com.teamabode.scribe.core.api.config.ConfigBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.conditions.v1.DefaultResourceConditions;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Guarding implements ModInitializer {
    public static final String MOD_ID = "guarding";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final EnchantmentCategory GUARDING_SHIELD = ClassTinkerers.getEnum(EnchantmentCategory.class, "GUARDING_SHIELD");

    public static final Config CONFIG = new ConfigBuilder(MOD_ID)
            .addGroup(GuardingConfig.GENERAL)
            .addGroup(GuardingConfig.PARRY)
            .addGroup(GuardingConfig.BARBED)
            .addGroup(GuardingConfig.PUMMELING)
            .addGroup(GuardingConfig.RETRIBUTION)
            .build();

    public void onInitialize() {
        GuardingItems.init();
        GuardingEnchantments.init();
        GuardingSounds.init();
        GuardingParticles.init();
        GuardingEvents.init();
        DefaultResourceConditions.featuresEnabled();
        ResourceConditions.register(modPrefix("netherite_shield_enabled"), root -> CONFIG.getGroup("general").getBooleanProperty("netherite_shield_enabled"));
    }

    public static ResourceLocation modPrefix(String identifier) {
        return new ResourceLocation(MOD_ID, identifier);
    }
}
