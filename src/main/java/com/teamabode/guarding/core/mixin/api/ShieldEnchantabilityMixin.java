package com.teamabode.guarding.core.mixin.api;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShieldItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ShieldItem.class)
public class ShieldEnchantabilityMixin extends Item {
    public ShieldEnchantabilityMixin(Properties properties) {
        super(properties);
    }

    public int getEnchantmentValue() {
        return 9;
    }
}
