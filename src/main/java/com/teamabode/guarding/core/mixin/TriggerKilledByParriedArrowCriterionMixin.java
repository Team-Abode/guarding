package com.teamabode.guarding.core.mixin;

import com.teamabode.guarding.core.access.ProjectileAccessor;
import com.teamabode.guarding.core.registry.GuardingCriterions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class TriggerKilledByParriedArrowCriterionMixin {

    @Inject(method = "die", at = @At("HEAD"))
    private void guarding$die(DamageSource source, CallbackInfo ci) {
        LivingEntity $this = LivingEntity.class.cast(this);
        Entity directEntity = source.getDirectEntity();

        if (directEntity instanceof ProjectileAccessor projectile && projectile.getParrier() instanceof ServerPlayer player) {
            GuardingCriterions.KILLED_BY_PARRIED_ARROW.trigger(player, $this);
        }
    }
}
