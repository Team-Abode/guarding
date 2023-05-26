package com.teamabode.guarding.core.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public final class GuardingEvents {

    public static final Event<ShieldBlocked> SHIELD_BLOCKED = EventFactory.createArrayBacked(ShieldBlocked.class, callbacks -> (user, damageSource, amount) -> {
        for (var callback : callbacks) {
            callback.shieldBlocked(user, damageSource, amount);
        }
    });

    public static final Event<ShieldDisabled> SHIELD_DISABLED = EventFactory.createArrayBacked(ShieldDisabled.class, callbacks -> (user, attacker) -> {
        for (var callback : callbacks) {
            callback.shieldDisabled(user, attacker);
        }
    });

    @FunctionalInterface
    public interface ShieldBlocked {
        void shieldBlocked(Player user, DamageSource damageSource, float amount);
    }

    @FunctionalInterface
    public interface ShieldDisabled {
        void shieldDisabled(Player user, LivingEntity attacker);
    }
}
