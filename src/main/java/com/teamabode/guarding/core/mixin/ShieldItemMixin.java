package com.teamabode.guarding.core.mixin;

import com.teamabode.guarding.GuardingConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ShieldItem;
import org.spongepowered.asm.mixin.Mixin;

/**
 * This mixin handles Shield enchantability
 */
@Mixin(ShieldItem.class)
public class ShieldItemMixin extends Item {
    public ShieldItemMixin(net.minecraft.item.Item.Settings properties) {
        super(properties);
    }

    public int getEnchantability() {
        return GuardingConfig.INSTANCE.shieldEnchantibility.get();
    }
}
