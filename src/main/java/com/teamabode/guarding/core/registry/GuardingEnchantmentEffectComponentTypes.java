package com.teamabode.guarding.core.registry;

import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.common.enchantment.TargetedShieldEnchantmentEffect;
import java.util.List;
import java.util.function.UnaryOperator;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class GuardingEnchantmentEffectComponentTypes {
    public static final DataComponentType<EnchantmentValueEffect> SHIELD_KNOCKBACK = register("shield_knockback", builder -> builder.persistent(EnchantmentValueEffect.CODEC));

    public static final DataComponentType<List<TargetedShieldEnchantmentEffect<EnchantmentEntityEffect>>> SHIELD_BLOCKED = register("shield_blocked", builder -> builder.persistent(
            TargetedShieldEnchantmentEffect.codec(EnchantmentEntityEffect.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf()
    ));
    public static final DataComponentType<List<TargetedShieldEnchantmentEffect<EnchantmentEntityEffect>>> SHIELD_PARRIED = register("shield_parried", builder -> builder.persistent(
            TargetedShieldEnchantmentEffect.parriedCodec(EnchantmentEntityEffect.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf()
    ));

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> dataComponent) {
        return Registry.register(BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, Guarding.id(name), dataComponent.apply(DataComponentType.builder()).build());
    }

    public static void init() {

    }
}
