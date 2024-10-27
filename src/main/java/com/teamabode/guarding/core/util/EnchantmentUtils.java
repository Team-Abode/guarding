package com.teamabode.guarding.core.util;

import com.teamabode.guarding.common.enchantment.TargetedShieldEnchantmentEffect;
import com.teamabode.guarding.common.enchantment.EnchantmentConsumer;
import com.teamabode.guarding.core.registry.GuardingEnchantmentEffectComponentTypes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import org.apache.commons.lang3.mutable.MutableFloat;

public class EnchantmentUtils {

    public static float modifyParryKnockback(PlayerEntity player, float baseStrength) {
        MutableFloat strength = new MutableFloat(baseStrength);
        runIterationOnItem(player.getActiveItem(), (holder, level) -> {
            Enchantment enchantment = holder.value();
            enchantment.modifyValue(GuardingEnchantmentEffectComponentTypes.SHIELD_KNOCKBACK, player.getRandom(), level, strength);
        });
        return strength.floatValue();
    }

    public static void runBlockedEffects(ServerWorld server, PlayerEntity player, DamageSource source, boolean performedParry) {
        ItemStack stack = player.getActiveItem();
        runIterationOnItem(stack, (holder, level) -> {
            Enchantment enchantment = holder.value();
            EnchantmentEffectContext enchantedItem = new EnchantmentEffectContext(stack, getSlotFromHand(player.getActiveHand()), player);

            for (var blockedCondition : enchantment.getEffect(GuardingEnchantmentEffectComponentTypes.SHIELD_BLOCKED)) {
                if (performedParry && blockedCondition.cancelOnParry()) {
                    continue;
                }
                tryEffect(blockedCondition, server, level, enchantedItem, player, source);
            }
        });
    }

    public static void runParriedEffects(ServerWorld server, PlayerEntity player, DamageSource source) {
        ItemStack stack = player.getActiveItem();
        runIterationOnItem(stack, (holder, level) -> {
            Enchantment enchantment = holder.value();
            EnchantmentEffectContext enchantedItem = new EnchantmentEffectContext(stack, getSlotFromHand(player.getActiveHand()), player);

            for (var targetedCondition : enchantment.getEffect(GuardingEnchantmentEffectComponentTypes.SHIELD_PARRIED)) {
                tryEffect(targetedCondition, server, level, enchantedItem, player, source);
            }
        });
    }

    public static EquipmentSlot getSlotFromHand(Hand hand) {
        if (hand == Hand.MAIN_HAND) {
            return EquipmentSlot.MAINHAND;
        }
        return EquipmentSlot.OFFHAND;
    }

    public static void tryEffect(TargetedShieldEnchantmentEffect<EnchantmentEntityEffect> blockedCondition, ServerWorld server, int level, EnchantmentEffectContext enchantedItem, PlayerEntity player, DamageSource source) {
        if (blockedCondition.matches(Enchantment.createEnchantedDamageLootContext(server, level, player, source))) {
            Entity target = switch (blockedCondition.affected()) {
                case VICTIM -> player;
                case ATTACKER -> source.getAttacker();
                case DAMAGING_ENTITY -> source.getSource();
            };
            if (target != null) {
                blockedCondition.effect().apply(server, level, enchantedItem, target, target.getPos());
            }
        }
    }

    public static void runIterationOnItem(ItemStack stack, EnchantmentConsumer consumer) {
        ItemEnchantmentsComponent enchantments = stack.getOrDefault(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);

        for (var entry : enchantments.getEnchantmentEntries()) {
            consumer.accept(entry.getKey(), entry.getIntValue());
        }
    }
}
