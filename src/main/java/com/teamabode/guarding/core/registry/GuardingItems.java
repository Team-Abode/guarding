package com.teamabode.guarding.core.registry;

import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.common.item.NetheriteShieldItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class GuardingItems {

    public static final Item NETHERITE_SHIELD = register("netherite_shield", new NetheriteShieldItem(new Item.Settings().maxCount(1).maxDamage(614).fireproof()));

    private static <I extends Item> I register(String name, I item) {
        return Registry.register(Registries.ITEM, Guarding.id(name), item);
    }

    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(modifier -> {
            modifier.addAfter(Items.SHIELD, NETHERITE_SHIELD);
        });
    }
}
