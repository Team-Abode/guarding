package com.teamabode.guarding.common.item;

import com.teamabode.guarding.GuardingConfig;
import com.teamabode.guarding.core.registry.GuardingSounds;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;

public class NetheriteShieldItem extends ShieldItem {

    public NetheriteShieldItem(net.minecraft.world.item.Item.Properties properties) {
        super(properties);
    }

    public int getEnchantmentValue() {
        return GuardingConfig.INSTANCE.netheriteShieldEnchantibility.get();
    }

    public Holder<SoundEvent> getEquipSound() {
        return GuardingSounds.ITEM_NETHERITE_SHIELD_EQUIP;
    }

    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return repairCandidate.is(Items.NETHERITE_INGOT);
    }
}
