package com.teamabode.guarding.common.item;

import com.teamabode.guarding.GuardingConfig;
import com.teamabode.guarding.core.registry.GuardingSounds;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;

public class NetheriteShieldItem extends ShieldItem {

    public NetheriteShieldItem(net.minecraft.item.Item.Settings properties) {
        super(properties);
    }

    public int getEnchantability() {
        return GuardingConfig.INSTANCE.netheriteShieldEnchantibility.get();
    }

    public RegistryEntry<SoundEvent> getEquipSound() {
        return GuardingSounds.ITEM_NETHERITE_SHIELD_EQUIP;
    }

    public boolean canRepair(ItemStack stack, ItemStack repairCandidate) {
        return repairCandidate.isOf(Items.NETHERITE_INGOT);
    }
}
