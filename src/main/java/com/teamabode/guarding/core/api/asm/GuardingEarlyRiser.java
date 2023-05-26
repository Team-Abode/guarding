package com.teamabode.guarding.core.api.asm;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

public class GuardingEarlyRiser implements Runnable {

    public void run() {
        MappingResolver resolver = FabricLoader.getInstance().getMappingResolver();

        String enchantmentCategory = resolver.mapClassName("intermediary", "net.minecraft.class_1886");
        ClassTinkerers.enumBuilder(enchantmentCategory, new Class[0]).addEnumSubclass("GUARDING_SHIELD", "com.teamabode.guarding.core.api.asm.GuardingEnchantmentCategory").build();
    }
}
