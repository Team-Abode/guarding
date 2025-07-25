package com.teamabode.guarding.core.util;

import com.teamabode.guarding.GuardingConfig;
import com.teamabode.guarding.core.access.ProjectileAccessor;
import com.teamabode.guarding.core.registry.GuardingParticles;
import com.teamabode.guarding.core.registry.GuardingSounds;
import com.teamabode.guarding.core.registry.GuardingStats;
import com.teamabode.guarding.core.tag.GuardingDamageTypeTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;

public class ShieldUtils {

    public static void onBlocked(PlayerEntity user, DamageSource source, float amount) {
        if (!(user.getWorld() instanceof ServerWorld server)) return;
        boolean performedParry = canPerformParry(user, source);

        if (performedParry) {
            parry(server, user, source);
        }
        user.increaseStat(GuardingStats.ATTACKS_BLOCKED_BY_SHIELD, 1);
        EnchantmentUtils.runBlockedEffects(server, user, source, performedParry);
    }

    public static void parry(ServerWorld server, PlayerEntity player, DamageSource source) {
        if (source.getSource() instanceof ProjectileEntity projectile) {
            deflectProjectile(player, projectile);
            spawnParticle(server, projectile.getX(), projectile.getY(), projectile.getZ());
        }
        else if (source.getSource() instanceof LivingEntity attacker) {
            knockbackAttacker(player, attacker);
            spawnParticle(server, (player.getX() + attacker.getX()) / 2, attacker.getEyeY(), (player.getZ() + attacker.getZ()) / 2);
        }
        player.addExhaustion(GuardingConfig.INSTANCE.exhaustionCost.get());
        server.playSound(null, player.getBlockPos(), GuardingSounds.ITEM_SHIELD_PARRY, SoundCategory.PLAYERS);
        player.increaseStat(GuardingStats.ATTACKS_PARRIED_BY_SHIELD, 1);

        EnchantmentUtils.runParriedEffects(server, player, source);
    }

    public static void knockbackAttacker(PlayerEntity player, LivingEntity attacker) {
        float knockbackStrength = getKnockbackStrength(player);

        attacker.velocityModified = true;
        attacker.takeKnockback(knockbackStrength, player.getX() - attacker.getX(), player.getZ() - attacker.getZ());
    }

    public static void deflectProjectile(PlayerEntity player, ProjectileEntity projectile) {
        ((ProjectileAccessor) projectile).setParrier(player);

        projectile.setVelocity(projectile.getVelocity().multiply(5.0d));
        float reverseRot = 170.0F + projectile.getRandom().nextFloat() * 20.0F;
        projectile.setYaw(projectile.getYaw() + reverseRot);
        projectile.velocityDirty = true;
    }

    public static float getKnockbackStrength(PlayerEntity player) {
        float baseStrength = GuardingConfig.INSTANCE.knockbackStrength.get();
        return EnchantmentUtils.modifyParryKnockback(player, baseStrength);
    }

    public static void spawnParticle(ServerWorld server, double x, double y, double z) {
        server.spawnParticles(GuardingParticles.PARRY, x, y, z, 1, 0.0d, 0.0d, 0.0d, 0.0d);
    }

    public static boolean canPerformParry(PlayerEntity player, DamageSource source) {
        return player.getItemUseTime() <= 3 && !source.isIn(GuardingDamageTypeTags.NO_PARRY);
    }
}
