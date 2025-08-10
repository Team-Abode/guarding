package com.teamabode.guarding.core.registry;

import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.GuardingConstants;
import com.teamabode.guarding.common.component.ParriesAttacksComponent;
import com.teamabode.guarding.core.tag.GuardingDamageTypeTags;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlocksAttacksComponent;
import net.minecraft.component.type.BlocksAttacksComponent.DamageReduction;
import net.minecraft.component.type.BlocksAttacksComponent.ItemDamage;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.ItemTags;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class GuardingItems {

    public static final Item NETHERITE_SHIELD = register(
            "netherite_shield",
            ShieldItem::new,
            new Settings()
                    .maxCount(1)
                    .maxDamage(614)
                    .component(DataComponentTypes.EQUIPPABLE, EquippableComponent.builder(EquipmentSlot.OFFHAND)
                            .equipSound(GuardingSounds.ITEM_NETHERITE_SHIELD_EQUIP)
                            .swappable(false)
                            .build()
                    )
                    .component(DataComponentTypes.BLOCKS_ATTACKS, new BlocksAttacksComponent(
                            GuardingConstants.INSTANT_BLOCKING ? 0.0f : 0.25f,
                            1.0f,
                            List.of(
                                    new DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)
                            ),
                            new ItemDamage(3.0F, 1.0F, 1.0F),
                            Optional.of(DamageTypeTags.BYPASSES_SHIELD),
                            Optional.of(GuardingSounds.ITEM_NETHERITE_SHIELD_BLOCK),
                            Optional.of(GuardingSounds.ITEM_NETHERITE_SHIELD_BREAK)
                    ))
                    .component(GuardingDataComponentTypes.PARRIES_ATTACKS, new ParriesAttacksComponent(
                            0.25f,
                            GuardingConstants.KNOCKBACK_STRENGTH,
                            true,
                            Optional.of(GuardingSounds.ITEM_SHIELD_PARRY),
                            Optional.of(GuardingDamageTypeTags.CANNOT_PARRY)
                    ))
                    .component(DataComponentTypes.BREAK_SOUND, GuardingSounds.ITEM_NETHERITE_SHIELD_BREAK)
                    .enchantable(GuardingConstants.NETHERITE_SHIELD_ENCHANTABILITY)
                    .repairable(ItemTags.NETHERITE_TOOL_MATERIALS)
                    .fireproof()
    );

    private static Item register(String name, Function<Settings, Item> item, Item.Settings settings) {
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, Guarding.id(name));

        return Registry.register(Registries.ITEM, key, item.apply(settings.registryKey(key)));
    }

    private static Item register(String name, Settings settings) {
        return register(name, Item::new, settings);
    }

    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(modifier -> {
            modifier.addAfter(Items.SHIELD, NETHERITE_SHIELD);
        });
    }
}
