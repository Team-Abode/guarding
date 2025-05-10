package com.teamabode.guarding.common.enchantment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record TargetedShieldEnchantmentEffect<T>(EnchantmentTarget affected, T effect, boolean cancelOnParry, Optional<LootItemCondition> requirements) {

    public static <S> Codec<TargetedShieldEnchantmentEffect<S>> parriedCodec(Codec<S> codec, LootContextParamSet contextSet) {
        return RecordCodecBuilder.create(instance -> instance.group(
                EnchantmentTarget.CODEC.fieldOf("affected").forGetter(TargetedShieldEnchantmentEffect::affected),
                codec.fieldOf("effect").forGetter(TargetedShieldEnchantmentEffect::effect),
                ConditionalEffect.conditionCodec(contextSet).optionalFieldOf("requirements").forGetter(TargetedShieldEnchantmentEffect::requirements)
        ).apply(instance, (affected, effect, requirements) -> new TargetedShieldEnchantmentEffect<>(affected, effect, false, requirements)));
    }

    public static <S> Codec<TargetedShieldEnchantmentEffect<S>> codec(Codec<S> codec, LootContextParamSet contextSet) {
        return RecordCodecBuilder.create(instance -> instance.group(
                EnchantmentTarget.CODEC.fieldOf("affected").forGetter(TargetedShieldEnchantmentEffect::affected),
                codec.fieldOf("effect").forGetter(TargetedShieldEnchantmentEffect::effect),
                Codec.BOOL.fieldOf("cancel_on_parry").forGetter(TargetedShieldEnchantmentEffect::cancelOnParry),
                ConditionalEffect.conditionCodec(contextSet).optionalFieldOf("requirements").forGetter(TargetedShieldEnchantmentEffect::requirements)
        ).apply(instance, TargetedShieldEnchantmentEffect::new));
    }

    public boolean matches(LootContext lootContext) {
        return this.requirements.map(lootItemCondition -> lootItemCondition.test(lootContext)).orElse(true);
    }
}
