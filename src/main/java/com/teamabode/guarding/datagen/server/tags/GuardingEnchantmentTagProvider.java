package com.teamabode.guarding.datagen.server.tags;

import com.teamabode.guarding.core.registry.GuardingEnchantments;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.enchantment.Enchantment;
import java.util.concurrent.CompletableFuture;

public class GuardingEnchantmentTagProvider extends FabricTagProvider<Enchantment> {
    public GuardingEnchantmentTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.ENCHANTMENT, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        this.getOrCreateTagBuilder(EnchantmentTags.NON_TREASURE).setReplace(false)
                .add(GuardingEnchantments.BARBED)
                .add(GuardingEnchantments.PUMMELING);
    }
}
