package com.teamabode.guarding.core.event;

import com.teamabode.guarding.GuardingConstants;
import com.teamabode.guarding.common.component.ParriesAttacksComponent;
import com.teamabode.guarding.core.registry.GuardingDataComponentTypes;
import com.teamabode.guarding.core.registry.GuardingSounds;
import com.teamabode.guarding.core.tag.GuardingDamageTypeTags;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlocksAttacksComponent;
import net.minecraft.component.type.EnchantableComponent;
import net.minecraft.item.Items;

import java.util.List;
import java.util.Optional;

public class GuardingItemComponentEvents {

    public static void init() {
        DefaultItemComponentEvents.MODIFY.register(context -> {

            context.modify(Items.SHIELD, builder -> {

                builder.add(DataComponentTypes.ENCHANTABLE, new EnchantableComponent(GuardingConstants.SHIELD_ENCHANTABILITY));

                BlocksAttacksComponent blocksAttacks = getBlocksAttacksComponent(builder);

                builder.add(DataComponentTypes.BLOCKS_ATTACKS, new BlocksAttacksComponent(
                        GuardingConstants.INSTANT_BLOCKING ? 0.0f : blocksAttacks.blockDelaySeconds(),
                        blocksAttacks.disableCooldownScale(),
                        blocksAttacks.damageReductions(),
                        blocksAttacks.itemDamage(),
                        blocksAttacks.bypassedBy(),
                        blocksAttacks.blockSound(),
                        blocksAttacks.disableSound()
                ));

                builder.add(GuardingDataComponentTypes.PARRIES_ATTACKS, new ParriesAttacksComponent(
                        0.15f,
                        GuardingConstants.KNOCKBACK_STRENGTH,
                        true,
                        Optional.of(GuardingSounds.ITEM_SHIELD_PARRY),
                        Optional.of(GuardingDamageTypeTags.CANNOT_PARRY)
                ));
            });
        });
    }

    private static BlocksAttacksComponent getBlocksAttacksComponent(ComponentMap.Builder builder) {
        return builder.getOrDefault(DataComponentTypes.BLOCKS_ATTACKS, new BlocksAttacksComponent(
                0.0f,
                0,
                List.of(),
                BlocksAttacksComponent.ItemDamage.DEFAULT,
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        ));
    }
}
