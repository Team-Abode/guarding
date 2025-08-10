package com.teamabode.guarding.core.registry;

import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.common.enchantment.TargetedShieldEnchantmentEffect;
import com.teamabode.guarding.core.tag.GuardingItemTags;
import java.util.List;
import java.util.Optional;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.AllOfEnchantmentEffects;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.enchantment.effect.entity.ChangeItemDamageEnchantmentEffect;
import net.minecraft.enchantment.effect.entity.DamageEntityEnchantmentEffect;
import net.minecraft.enchantment.effect.value.AddEnchantmentEffect;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class GuardingEnchantments {
    public static final RegistryKey<Enchantment> BARBED = createKey("barbed");
    public static final RegistryKey<Enchantment> PUMMELING = createKey("pummeling");

    public static void register(Registerable<Enchantment> context) {
        context.register(BARBED, createBarbed(context));
        context.register(PUMMELING, createPummeling(context));
    }

    private static Enchantment createBarbed(Registerable<Enchantment> context) {
        var items = context.getRegistryLookup(RegistryKeys.ITEM);
        var damageTypes = context.getRegistryLookup(RegistryKeys.DAMAGE_TYPE);

        var definition = new Enchantment.Definition(
                items.getOrThrow(GuardingItemTags.SHIELD_ENCHANTABLE),
                Optional.empty(),
                2,
                1,
                new Enchantment.Cost(25, 25),
                new Enchantment.Cost(75, 25),
                8,
                List.of(AttributeModifierSlot.MAINHAND, AttributeModifierSlot.OFFHAND)
        );
        var enchantment = Enchantment.builder(definition);
        var effects = AllOfEnchantmentEffects.allOf(
                new DamageEntityEnchantmentEffect(EnchantmentLevelBasedValue.constant(3.0f), EnchantmentLevelBasedValue.constant(3.0f), damageTypes.getOrThrow(DamageTypes.THORNS)),
                new ChangeItemDamageEnchantmentEffect(EnchantmentLevelBasedValue.constant(1.0f))
        );
        enchantment.addNonListEffect(GuardingEnchantmentEffectComponentTypes.SHIELD_BLOCKED, List.of(
                new TargetedShieldEnchantmentEffect<>(EnchantmentEffectTarget.ATTACKER, effects, true, Optional.of(RandomChanceLootCondition.builder(0.33f).build()))
        ));
        enchantment.addNonListEffect(GuardingEnchantmentEffectComponentTypes.SHIELD_PARRIED, List.of(
                new TargetedShieldEnchantmentEffect<>(EnchantmentEffectTarget.ATTACKER, effects, false, Optional.empty())
        ));
        return enchantment.build(BARBED.getValue());
    }

    private static Enchantment createPummeling(Registerable<Enchantment> context) {
        var items = context.getRegistryLookup(RegistryKeys.ITEM);

        var definition = new Enchantment.Definition(
                items.getOrThrow(GuardingItemTags.SHIELD_ENCHANTABLE),
                Optional.empty(),
                5,
                3,
                new Enchantment.Cost(5, 8),
                new Enchantment.Cost(55, 8),
                4,
                List.of(AttributeModifierSlot.MAINHAND, AttributeModifierSlot.OFFHAND)
        );
        var enchantment = Enchantment.builder(definition);
        enchantment.addNonListEffect(GuardingEnchantmentEffectComponentTypes.SHIELD_KNOCKBACK, new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.15f)));

        return enchantment.build(PUMMELING.getValue());
    }

    private static RegistryKey<Enchantment> createKey(String name) {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, Guarding.id(name));
    }
}
