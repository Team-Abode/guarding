package com.teamabode.guarding.datagen.server.tags;

import com.teamabode.guarding.core.registry.GuardingEnchantments;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EnchantmentTags;
import java.util.concurrent.CompletableFuture;

public class GuardingEnchantmentTagProvider extends FabricTagProvider<Enchantment> {
    public GuardingEnchantmentTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.ENCHANTMENT, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        this.getOrCreateTagBuilder(EnchantmentTags.NON_TREASURE).setReplace(false)
                .add(GuardingEnchantments.BARBED)
                .add(GuardingEnchantments.PUMMELING);
    }
}
