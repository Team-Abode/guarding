package com.teamabode.guarding.core.util;

import com.teamabode.guarding.GuardingConfig;
import com.teamabode.guarding.core.access.ProjectileAccessor;
import com.teamabode.guarding.core.registry.GuardingParticles;
import com.teamabode.guarding.core.registry.GuardingSounds;
import com.teamabode.guarding.core.registry.GuardingStats;
import com.teamabode.guarding.core.tag.GuardingDamageTypeTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;

public class ShieldUtils {

    public static void onBlocked(Player user, DamageSource source, float amount) {
        if (!(user.level() instanceof ServerLevel server)) return;
        boolean performedParry = canPerformParry(user, source);

        if (performedParry) {
            parry(server, user, source);
        }
        user.awardStat(GuardingStats.ATTACKS_BLOCKED_BY_SHIELD, 1);
        EnchantmentUtils.runBlockedEffects(server, user, source, performedParry);
    }

    public static void parry(ServerLevel server, Player player, DamageSource source) {
        if (source.getDirectEntity() instanceof Projectile projectile) {
            deflectProjectile(player, projectile);
            spawnParticle(server, projectile.getX(), projectile.getY(), projectile.getZ());
        }
        else if (source.getDirectEntity() instanceof LivingEntity attacker) {
            knockbackAttacker(player, attacker);
            spawnParticle(server, (player.getX() + attacker.getX()) / 2, attacker.getEyeY(), (player.getZ() + attacker.getZ()) / 2);
        }
        player.causeFoodExhaustion(GuardingConfig.INSTANCE.exhaustionCost.get());
        server.playSound(null, player.blockPosition(), GuardingSounds.ITEM_SHIELD_PARRY, SoundSource.PLAYERS);
        player.awardStat(GuardingStats.ATTACKS_PARRIED_BY_SHIELD, 1);

        EnchantmentUtils.runParriedEffects(server, player, source);
    }

    public static void knockbackAttacker(Player player, LivingEntity attacker) {
        float knockbackStrength = getKnockbackStrength(player);

        attacker.hurtMarked = true;
        attacker.knockback(knockbackStrength, player.getX() - attacker.getX(), player.getZ() - attacker.getZ());
    }

    public static void deflectProjectile(Player player, Projectile projectile) {
        ((ProjectileAccessor) projectile).setParrier(player);

        projectile.setDeltaMovement(projectile.getDeltaMovement().scale(5.0d));
        float reverseRot = 170.0F + projectile.getRandom().nextFloat() * 20.0F;
        projectile.setYRot(projectile.getYRot() + reverseRot);
        projectile.hasImpulse = true;
    }

    public static float getKnockbackStrength(Player player) {
        float baseStrength = GuardingConfig.INSTANCE.knockbackStrength.get();
        return EnchantmentUtils.modifyParryKnockback(player, baseStrength);
    }

    public static void spawnParticle(ServerLevel server, double x, double y, double z) {
        server.sendParticles(GuardingParticles.PARRY, x, y, z, 1, 0.0d, 0.0d, 0.0d, 0.0d);
    }

    public static boolean canPerformParry(Player player, DamageSource source) {
        return player.getTicksUsingItem() <= 3 && !source.is(GuardingDamageTypeTags.NO_PARRY);
    }
}
