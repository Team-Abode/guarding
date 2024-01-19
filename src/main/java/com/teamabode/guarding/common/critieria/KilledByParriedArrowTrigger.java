package com.teamabode.guarding.common.critieria;

import com.google.gson.JsonObject;
import com.teamabode.guarding.Guarding;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;

public class KilledByParriedArrowTrigger extends SimpleCriterionTrigger<KilledByParriedArrowTrigger.TriggerInstance> {
    private static final ResourceLocation ID = Guarding.id("killed_by_parried_arrow");

    @Override
    protected TriggerInstance createInstance(JsonObject json, ContextAwarePredicate predicate, DeserializationContext deserializationContext) {
        return new TriggerInstance(predicate, EntityPredicate.fromJson(json, "victim", deserializationContext));
    }

    public void trigger(ServerPlayer player, Entity entity) {
        LootContext context = EntityPredicate.createContext(player, entity);
        Guarding.LOGGER.info(context.toString());
        this.trigger(player, triggerInstance -> triggerInstance.matches(context));
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final ContextAwarePredicate victimPredicate;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate, ContextAwarePredicate victimPredicate) {
            super(ID, contextAwarePredicate);
            this.victimPredicate = victimPredicate;
        }

        public boolean matches(LootContext context) {
            return this.victimPredicate.matches(context);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context) {
            JsonObject root = super.serializeToJson(context);
            root.add("victim", this.victimPredicate.toJson(context));
            return root;
        }
    }
}
