package com.teamabode.guarding;

import com.teamabode.guarding.core.event.GuardingItemComponentEvents;
import com.teamabode.guarding.core.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Guarding implements ModInitializer {
    public static final String MOD_ID = "guarding";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public void onInitialize() {
        GuardingItems.init();
        GuardingEnchantmentEffectComponentTypes.init();
        GuardingSounds.init();
        GuardingParticles.init();
        GuardingCriterions.init();
        GuardingDataComponentTypes.init();

        GuardingItemComponentEvents.init();
    }

    public static Identifier id(String name) {
        return Identifier.of(MOD_ID, name);
    }

    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
