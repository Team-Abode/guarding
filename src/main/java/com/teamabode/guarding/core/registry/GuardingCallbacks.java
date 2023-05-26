package com.teamabode.guarding.core.registry;

import com.teamabode.guarding.core.api.GuardingEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import java.util.List;

public class GuardingCallbacks {

    public static void init() {
        GuardingEvents.SHIELD_BLOCKED.register(GuardingCallbacks::onShieldBlock);
        GuardingEvents.SHIELD_DISABLED.register(GuardingCallbacks::onShieldDisabled);
    }

    private static void onShieldBlock(Player user, DamageSource source, float amount) {
        Entity sourceEntity = source.getDirectEntity();
        boolean isParry = (user.getUseItem().getUseDuration() - user.getUseItemRemainingTicks()) <= 2;

        if (sourceEntity instanceof LivingEntity attacker) {
            if (isParry) {
                float strength = getKnockbackStrength(user.getUseItem());
                attacker.knockback(strength, user.getX() - attacker.getX(), user.getZ() - attacker.getZ());
                attacker.hurtMarked = true;
                isParry = true;
            }
            handleBarbed(user, attacker, isParry);
        }
        if (sourceEntity instanceof Projectile projectile) {
            projectile.discard();
        }
        if (isParry) parryEffects(user, sourceEntity);
    }

    private static void handleBarbed(Player user, LivingEntity attacker, boolean isParry) {
        RandomSource random = user.getRandom();
        float chance = 0.15f, damage = 2.0f; // Will be changed later when config is implemented
        damage += isParry ? 1.0f : 0.0f;

        if (EnchantmentHelper.getItemEnchantmentLevel(GuardingEnchantments.BARBED, user.getUseItem()) <= 0) return;
        if (random.nextFloat() > chance && !isParry) return;

        attacker.hurt(attacker.damageSources().thorns(user), damage);
    }

    private static float getKnockbackStrength(ItemStack stack) {
        float strength = 0.5f; // Will be changed later when config is implemented
        int pummelLevel = EnchantmentHelper.getItemEnchantmentLevel(GuardingEnchantments.PUMMELING, stack);
        float pummelStrength = pummelLevel > 0 ? pummelLevel * 0.15f : 0.0f;

        return strength + pummelStrength;
    }

    private static void parryEffects(Player user, Entity sourceEntity) {
        Level level = user.getLevel();
        level.playSound(null, user.blockPosition(), GuardingSounds.ITEM_SHIELD_PARRY, SoundSource.PLAYERS);

        if (level instanceof ServerLevel server && sourceEntity != null) {
            server.sendParticles(GuardingParticles.PARRY, sourceEntity.getX(), sourceEntity.getEyeY(), sourceEntity.getZ(), 1, 0.0d, 0.0d, 0.0d, 0.0d);
        }
    }

    private static void onShieldDisabled(Player user, LivingEntity attacker) {
        ItemStack useItem = user.getUseItem();
        int retributionLevel = EnchantmentHelper.getItemEnchantmentLevel(GuardingEnchantments.RETRIBUTION, useItem);

        if (retributionLevel > 0) {
            List<LivingEntity> list = attacker.getLevel().getEntitiesOfClass(LivingEntity.class, user.getBoundingBox().inflate(3.0d, 0.0d, 3.0d), livingEntity -> isEnemy(user, livingEntity));

            for (LivingEntity livingEntity : list) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, retributionLevel * 100, 2, false, true));
            }
        }
    }

    private static boolean isEnemy(Player user, LivingEntity other) {
        if (other == user) return false;
        if (other instanceof Player otherPlayer) return user.canHarmPlayer(otherPlayer);
        return other.canBeSeenAsEnemy();
    }
}
