package com.teamabode.guarding.core.mixin;

import com.teamabode.guarding.core.access.ProjectileAccessor;
import com.teamabode.guarding.core.registry.GuardingCriterions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class TriggerKilledByParriedArrowCriterionMixin {

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void guarding$onDeath(DamageSource source, CallbackInfo ci) {
        LivingEntity $this = LivingEntity.class.cast(this);
        Entity directEntity = source.getSource();

        if (directEntity instanceof ProjectileAccessor projectile && projectile.getParrier() instanceof ServerPlayerEntity player) {
            GuardingCriterions.KILLED_BY_PARRIED_ARROW.trigger(player, $this);
        }
    }
}
