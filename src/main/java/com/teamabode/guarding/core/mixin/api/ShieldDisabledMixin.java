package com.teamabode.guarding.core.mixin.api;

import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.core.api.GuardingEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class ShieldDisabledMixin {

    @Inject(method = "blockUsingShield", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;disableShield(Z)V", shift = At.Shift.BEFORE))
    private void shieldDisabledEvent(LivingEntity attacker, CallbackInfo ci) {
        Guarding.LOGGER.info("Shield disabled!");
        GuardingEvents.SHIELD_DISABLED.invoker().shieldDisabled(Player.class.cast(this), attacker);
    }
}
