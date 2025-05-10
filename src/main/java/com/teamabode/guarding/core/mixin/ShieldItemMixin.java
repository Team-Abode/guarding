package com.teamabode.guarding.core.mixin;

import com.teamabode.guarding.GuardingConfig;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShieldItem;
import org.spongepowered.asm.mixin.Mixin;

/**
 * This mixin handles Shield enchantability
 */
@Mixin(ShieldItem.class)
public class ShieldItemMixin extends Item {
    public ShieldItemMixin(net.minecraft.world.item.Item.Properties properties) {
        super(properties);
    }

    public int getEnchantmentValue() {
        return GuardingConfig.INSTANCE.shieldEnchantibility.get();
    }
}
