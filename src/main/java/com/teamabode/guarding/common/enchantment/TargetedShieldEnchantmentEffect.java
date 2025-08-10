package com.teamabode.guarding.common.enchantment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.enchantment.effect.EnchantmentEffectEntry;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.context.ContextType;

public record TargetedShieldEnchantmentEffect<T>(EnchantmentEffectTarget affected, T effect, boolean cancelOnParry, Optional<LootCondition> requirements) {

    public static <S> Codec<TargetedShieldEnchantmentEffect<S>> parriedCodec(Codec<S> codec, ContextType context) {
        return RecordCodecBuilder.create(instance -> instance.group(
                EnchantmentEffectTarget.CODEC.fieldOf("affected").forGetter(TargetedShieldEnchantmentEffect::affected),
                codec.fieldOf("effect").forGetter(TargetedShieldEnchantmentEffect::effect),
                EnchantmentEffectEntry.createRequirementsCodec(context).optionalFieldOf("requirements").forGetter(TargetedShieldEnchantmentEffect::requirements)
        ).apply(instance, (affected, effect, requirements) -> new TargetedShieldEnchantmentEffect<>(affected, effect, false, requirements)));
    }

    public static <S> Codec<TargetedShieldEnchantmentEffect<S>> codec(Codec<S> codec, ContextType context) {
        return RecordCodecBuilder.create(instance -> instance.group(
                EnchantmentEffectTarget.CODEC.fieldOf("affected").forGetter(TargetedShieldEnchantmentEffect::affected),
                codec.fieldOf("effect").forGetter(TargetedShieldEnchantmentEffect::effect),
                Codec.BOOL.fieldOf("cancel_on_parry").forGetter(TargetedShieldEnchantmentEffect::cancelOnParry),
                EnchantmentEffectEntry.createRequirementsCodec(context).optionalFieldOf("requirements").forGetter(TargetedShieldEnchantmentEffect::requirements)
        ).apply(instance, TargetedShieldEnchantmentEffect::new));
    }

    public boolean matches(LootContext lootContext) {
        return this.requirements.map(lootItemCondition -> lootItemCondition.test(lootContext)).orElse(true);
    }
}
