package com.teamabode.guarding.common.critieria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.CriterionValidator;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;

public class KilledByParriedArrowCriterion extends SimpleCriterionTrigger<KilledByParriedArrowCriterion.TriggerInstance> {

    public void trigger(ServerPlayer player, Entity victim) {
        LootContext context = EntityPredicate.createContext(player, victim);
        this.trigger(player, triggerInstance -> triggerInstance.matches(context));
    }

    @Override
    public Codec<TriggerInstance> codec() {
        return KilledByParriedArrowCriterion.TriggerInstance.CODEC;
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> victim) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("victim").forGetter(TriggerInstance::victim)
        ).apply(instance, TriggerInstance::new));

        public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> victim) {
            this.player = player;
            this.victim = victim;
        }

        public boolean matches(LootContext context) {
            return this.victim.isEmpty() || this.victim.get().matches(context);
        }

        @Override
        public void validate(CriterionValidator criterionValidator) {
            criterionValidator.validateEntity(this.player, ".player");
            criterionValidator.validateEntity(this.victim, ".victim");
        }
    }
}
