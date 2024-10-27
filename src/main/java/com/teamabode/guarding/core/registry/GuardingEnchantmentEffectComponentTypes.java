package com.teamabode.guarding.core.registry;

import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.common.enchantment.TargetedShieldEnchantmentEffect;
import java.util.List;
import java.util.function.UnaryOperator;
import net.minecraft.component.ComponentType;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class GuardingEnchantmentEffectComponentTypes {
    public static final ComponentType<EnchantmentValueEffect> SHIELD_KNOCKBACK = register("shield_knockback", builder -> builder.codec(EnchantmentValueEffect.CODEC));

    public static final ComponentType<List<TargetedShieldEnchantmentEffect<EnchantmentEntityEffect>>> SHIELD_BLOCKED = register("shield_blocked", builder -> builder.codec(
            TargetedShieldEnchantmentEffect.codec(EnchantmentEntityEffect.CODEC, LootContextTypes.ENCHANTED_DAMAGE).listOf()
    ));
    public static final ComponentType<List<TargetedShieldEnchantmentEffect<EnchantmentEntityEffect>>> SHIELD_PARRIED = register("shield_parried", builder -> builder.codec(
            TargetedShieldEnchantmentEffect.parriedCodec(EnchantmentEntityEffect.CODEC, LootContextTypes.ENCHANTED_DAMAGE).listOf()
    ));

    private static <T> ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> dataComponent) {
        return Registry.register(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, Guarding.id(name), dataComponent.apply(ComponentType.builder()).build());
    }

    public static void init() {

    }
}
