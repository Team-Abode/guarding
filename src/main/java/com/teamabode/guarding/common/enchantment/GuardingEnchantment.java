package com.teamabode.guarding.common.enchantment;

import com.teamabode.guarding.Guarding;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.Enchantment;

public class GuardingEnchantment extends Enchantment {

    public GuardingEnchantment(Rarity rarity) {
        super(rarity, Guarding.GUARDING_SHIELD, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    public boolean canEnchant(ItemStack stack) {
        return stack.is(ConventionalItemTags.SHIELDS) || stack.getItem() instanceof ShieldItem;
    }
}
