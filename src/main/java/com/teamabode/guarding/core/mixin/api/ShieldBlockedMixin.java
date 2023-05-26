package com.teamabode.guarding.core.mixin.api;

import com.teamabode.guarding.core.api.GuardingEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class ShieldBlockedMixin {

    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;blockUsingShield(Lnet/minecraft/world/entity/LivingEntity;)V", shift = At.Shift.BEFORE))
    private void shieldBlockEvent(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callback) {
        LivingEntity livingEntity = LivingEntity.class.cast(this);

        if (livingEntity instanceof Player player) {
            GuardingEvents.SHIELD_BLOCKED.invoker().shieldBlocked(player, source, amount);
        }
    }
}
