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
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import org.apache.commons.lang3.mutable.MutableFloat;

public class EnchantmentUtils {

    public static float modifyParryKnockback(ItemStack stack, LivingEntity user, float baseStrength) {
        MutableFloat strength = new MutableFloat(baseStrength);
        runIterationOnItem(stack, (holder, level) -> {
            Enchantment enchantment = holder.value();
            enchantment.modifyValue(GuardingEnchantmentEffectComponentTypes.SHIELD_KNOCKBACK, user.getRandom(), level, strength);
        });
        return strength.floatValue();
    }

    public static void runBlockedEffects(ServerWorld server, LivingEntity user, ItemStack stack, DamageSource source, boolean performedParry) {
        runIterationOnItem(stack, (holder, level) -> {
            Enchantment enchantment = holder.value();
            EnchantmentEffectContext enchantedItem = new EnchantmentEffectContext(stack, LivingEntity.getSlotForHand(user.getActiveHand()), user);

            for (var blockedCondition : enchantment.getEffect(GuardingEnchantmentEffectComponentTypes.SHIELD_BLOCKED)) {
                if (performedParry && blockedCondition.cancelOnParry()) {
                    continue;
                }
                tryEffect(blockedCondition, server, level, enchantedItem, user, source);
            }
        });


    }

    public static void runParriedEffects(ServerWorld server, LivingEntity user, ItemStack stack, DamageSource source) {
        runIterationOnItem(stack, (holder, level) -> {
            Enchantment enchantment = holder.value();
            EnchantmentEffectContext enchantedItem = new EnchantmentEffectContext(stack, LivingEntity.getSlotForHand(user.getActiveHand()), user);

            for (var targetedCondition : enchantment.getEffect(GuardingEnchantmentEffectComponentTypes.SHIELD_PARRIED)) {
                tryEffect(targetedCondition, server, level, enchantedItem, user, source);
            }
        });
    }

    public static void tryEffect(TargetedShieldEnchantmentEffect<EnchantmentEntityEffect> blockedCondition, ServerWorld server, int level, EnchantmentEffectContext enchantedItem, LivingEntity user, DamageSource source) {
        if (blockedCondition.matches(Enchantment.createEnchantedDamageLootContext(server, level, user, source))) {
            Entity target = switch (blockedCondition.affected()) {
                case VICTIM -> user;
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
