package com.teamabode.guarding.core.mixin.gameplay;

import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.core.access.ProjectileAccessor;
import com.teamabode.guarding.core.init.GuardingCritieriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class KilledByParriedArrowTriggerMixin {

    @Inject(method = "die", at = @At("HEAD"))
    private void die(DamageSource source, CallbackInfo ci) {
        LivingEntity $this = LivingEntity.class.cast(this);
        Entity directEntity = source.getDirectEntity();

        if (directEntity instanceof ProjectileAccessor projectile && projectile.getParrier() instanceof ServerPlayer player) {
            Guarding.LOGGER.info(player.getStringUUID());
            GuardingCritieriaTriggers.KILLED_BY_PARRIED_ARROW.trigger(player, $this);
        }
    }
}
