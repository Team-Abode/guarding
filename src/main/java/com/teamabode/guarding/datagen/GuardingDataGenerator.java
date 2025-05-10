package com.teamabode.guarding.datagen;

import com.teamabode.guarding.core.registry.GuardingEnchantments;
import com.teamabode.guarding.datagen.server.GuardingDynamicRegistryProvider;
import com.teamabode.guarding.datagen.server.GuardingAdvancementProvider;
import com.teamabode.guarding.datagen.server.tags.GuardingDamageTypeTagProvider;
import com.teamabode.guarding.datagen.server.tags.GuardingEnchantmentTagProvider;
import com.teamabode.guarding.datagen.server.tags.GuardingItemTagProvider;
import com.teamabode.guarding.datagen.server.GuardingRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class GuardingDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        pack.addProvider(GuardingAdvancementProvider::new);
        pack.addProvider(GuardingRecipeProvider::new);
        pack.addProvider(GuardingDynamicRegistryProvider::new);

        // Tags
        pack.addProvider(GuardingItemTagProvider::new);
        pack.addProvider(GuardingDamageTypeTagProvider::new);
        pack.addProvider(GuardingEnchantmentTagProvider::new);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder builder) {
        builder.add(Registries.ENCHANTMENT, GuardingEnchantments::register);
    }
}
