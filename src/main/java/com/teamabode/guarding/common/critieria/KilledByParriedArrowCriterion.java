package com.teamabode.guarding.common.critieria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.entity.Entity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.entity.LootContextPredicateValidator;
import net.minecraft.server.network.ServerPlayerEntity;

public class KilledByParriedArrowCriterion extends AbstractCriterion<KilledByParriedArrowCriterion.TriggerInstance> {

    public void trigger(ServerPlayerEntity player, Entity victim) {
        LootContext context = EntityPredicate.createAdvancementEntityLootContext(player, victim);
        this.trigger(player, triggerInstance -> triggerInstance.matches(context));
    }

    @Override
    public Codec<TriggerInstance> getConditionsCodec() {
        return KilledByParriedArrowCriterion.TriggerInstance.CODEC;
    }

    public record TriggerInstance(Optional<LootContextPredicate> player, Optional<LootContextPredicate> victim) implements AbstractCriterion.Conditions {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("victim").forGetter(TriggerInstance::victim)
        ).apply(instance, TriggerInstance::new));

        public TriggerInstance(Optional<LootContextPredicate> player, Optional<LootContextPredicate> victim) {
            this.player = player;
            this.victim = victim;
        }

        public boolean matches(LootContext context) {
            return this.victim.isEmpty() || this.victim.get().test(context);
        }

        @Override
        public void validate(LootContextPredicateValidator criterionValidator) {
            criterionValidator.validateEntityPredicate(this.player, ".player");
            criterionValidator.validateEntityPredicate(this.victim, ".victim");
        }
    }
}
