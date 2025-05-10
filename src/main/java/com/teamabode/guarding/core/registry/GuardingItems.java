package com.teamabode.guarding.core.registry;

import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.common.item.NetheriteShieldItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class GuardingItems {

    public static final Item NETHERITE_SHIELD = register("netherite_shield", new NetheriteShieldItem(new Item.Properties().stacksTo(1).durability(614).fireResistant()));

    private static <I extends Item> I register(String name, I item) {
        return Registry.register(BuiltInRegistries.ITEM, Guarding.id(name), item);
    }

    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register(modifier -> {
            modifier.addAfter(Items.SHIELD, NETHERITE_SHIELD);
        });
    }
}
