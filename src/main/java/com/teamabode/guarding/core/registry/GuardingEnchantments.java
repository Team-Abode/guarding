package com.teamabode.guarding.core.registry;

import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.common.enchantment.TargetedShieldEnchantmentEffect;
import com.teamabode.guarding.core.tag.GuardingItemTags;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantment.Builder;
import net.minecraft.world.item.enchantment.Enchantment.EnchantmentDefinition;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.item.enchantment.effects.AllOf;
import net.minecraft.world.item.enchantment.effects.AllOf.EntityEffects;
import net.minecraft.world.item.enchantment.effects.DamageEntity;
import net.minecraft.world.item.enchantment.effects.DamageItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;

public class GuardingEnchantments {
    public static final ResourceKey<Enchantment> BARBED = createKey("barbed");
    public static final ResourceKey<Enchantment> PUMMELING = createKey("pummeling");

    public static void register(BootstrapContext<Enchantment> context) {
        context.register(BARBED, createBarbed(context));
        context.register(PUMMELING, createPummeling(context));
    }

    private static Enchantment createBarbed(BootstrapContext<Enchantment> context) {
        var items = context.lookup(Registries.ITEM);
        var damageTypes = context.lookup(Registries.DAMAGE_TYPE);

        var definition = new Enchantment.EnchantmentDefinition(
                items.getOrThrow(GuardingItemTags.SHIELD_ENCHANTABLE),
                Optional.empty(),
                2,
                1,
                new Enchantment.Cost(25, 25),
                new Enchantment.Cost(75, 25),
                8,
                List.of(EquipmentSlotGroup.MAINHAND, EquipmentSlotGroup.OFFHAND)
        );
        var enchantment = Enchantment.enchantment(definition);
        var effects = AllOf.entityEffects(
                new DamageEntity(LevelBasedValue.constant(3.0f), LevelBasedValue.constant(3.0f), damageTypes.getOrThrow(DamageTypes.THORNS)),
                new DamageItem(LevelBasedValue.constant(1.0f))
        );
        enchantment.withSpecialEffect(GuardingEnchantmentEffectComponentTypes.SHIELD_BLOCKED, List.of(
                new TargetedShieldEnchantmentEffect<>(EnchantmentTarget.ATTACKER, effects, true, Optional.of(LootItemRandomChanceCondition.randomChance(0.33f).build()))
        ));
        enchantment.withSpecialEffect(GuardingEnchantmentEffectComponentTypes.SHIELD_PARRIED, List.of(
                new TargetedShieldEnchantmentEffect<>(EnchantmentTarget.ATTACKER, effects, false, Optional.empty())
        ));
        return enchantment.build(BARBED.location());
    }

    private static Enchantment createPummeling(BootstrapContext<Enchantment> context) {
        var items = context.lookup(Registries.ITEM);

        var definition = new Enchantment.EnchantmentDefinition(
                items.getOrThrow(GuardingItemTags.SHIELD_ENCHANTABLE),
                Optional.empty(),
                5,
                3,
                new Enchantment.Cost(5, 8),
                new Enchantment.Cost(55, 8),
                4,
                List.of(EquipmentSlotGroup.MAINHAND, EquipmentSlotGroup.OFFHAND)
        );
        var enchantment = Enchantment.enchantment(definition);
        enchantment.withSpecialEffect(GuardingEnchantmentEffectComponentTypes.SHIELD_KNOCKBACK, new AddValue(LevelBasedValue.perLevel(0.15f)));

        return enchantment.build(PUMMELING.location());
    }

    private static ResourceKey<Enchantment> createKey(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT, Guarding.id(name));
    }
}
