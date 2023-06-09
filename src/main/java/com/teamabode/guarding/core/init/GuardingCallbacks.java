package com.teamabode.guarding.core.init;

import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.core.api.GuardingEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
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
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class GuardingCallbacks {

    public static void init() {
        GuardingEvents.SHIELD_BLOCKED.register(GuardingCallbacks::onShieldBlock);
        GuardingEvents.SHIELD_DISABLED.register(GuardingCallbacks::onShieldDisabled);
    }

    // Logic for blocking
    private static void onShieldBlock(Player user, DamageSource source, float amount) {
        Entity sourceEntity = source.getDirectEntity();
        boolean isParry = (user.getUseItem().getUseDuration() - user.getUseItemRemainingTicks()) <= 2;
        if (sourceEntity instanceof LivingEntity attacker) blockLivingEntity(user, attacker, isParry);
        if (sourceEntity instanceof Projectile projectile) blockProjectile(user, source.getEntity(), projectile, isParry);
        tryParryEffects(user, sourceEntity, isParry);
    }

    // Logic for blocking a living entity
    private static void blockLivingEntity(Player user, LivingEntity attacker, boolean isParry) {
        if (isParry) {
            float exhaustion = Guarding.CONFIG.getGroup("parry").getFloatProperty("exhaustion_cost");
            float strength = getKnockbackStrength(user.getUseItem());

            user.causeFoodExhaustion(exhaustion);
            attacker.knockback(strength, user.getX() - attacker.getX(), user.getZ() - attacker.getZ());
            attacker.hurtMarked = true;
        }
        handleBarbed(user, attacker, isParry);
    }

    // Logic for blocking a projectile
    private static void blockProjectile(Player user, Entity damageCauser, Projectile projectile, boolean isParry) {
        if (!isParry || damageCauser == null) return;
        float projectileLaunchStrength = Guarding.CONFIG.getGroup("parry").getFloatProperty("projectile_launch_strength");

        Vec3 motion = new Vec3(user.getX() - damageCauser.getX(), 0.0f, user.getZ() - damageCauser.getZ()).scale(projectileLaunchStrength);
        projectile.setDeltaMovement(motion.x(), -1.5f, motion.z());
        projectile.hurtMarked = true;
    }

    // Handles all code for the barbed enchantment
    private static void handleBarbed(Player user, LivingEntity attacker, boolean isParry) {
        RandomSource random = user.getRandom();
        float damage = Guarding.CONFIG.getGroup("barbed").getFloatProperty("damage_amount");
        float chance = Guarding.CONFIG.getGroup("barbed").getFloatProperty("damage_chance");
        int barbedLevel = EnchantmentHelper.getItemEnchantmentLevel(GuardingEnchantments.BARBED, user.getUseItem());
        if (barbedLevel <= 0) return;
        damage += isParry ? 1.0f : 0.0f; // Parrying will cause barbed to increase it's power.

        if ((random.nextFloat() <= chance && chance > 0.0f) || isParry || chance >= 1.0f) {
            attacker.hurt(attacker.damageSources().thorns(user), damage);
            user.hurtCurrentlyUsedShield(2.0f);
        }
    }

    // Determines the knockback strength on parry
    private static float getKnockbackStrength(ItemStack stack) {
        float baseStrength = Guarding.CONFIG.getGroup("parry").getFloatProperty("knockback_strength");
        float additionalStrength = Guarding.CONFIG.getGroup("pummeling").getFloatProperty("additional_knockback_strength_per_level");
        int pummelingLevel = EnchantmentHelper.getItemEnchantmentLevel(GuardingEnchantments.PUMMELING, stack);
        float pummelStrength = pummelingLevel > 0 ? pummelingLevel * additionalStrength : 0.0f;

        return baseStrength + pummelStrength;
    }

    // Parry visual effects (Sound and particles)
    private static void tryParryEffects(Player user, Entity sourceEntity, boolean isParry) {
        if (!isParry) return;

        Level level = user.level();
        SoundEvent breakSound = user.getUseItem().is(GuardingItems.NETHERITE_SHIELD) ? GuardingSounds.ITEM_NETHERITE_SHIELD_PARRY : GuardingSounds.ITEM_SHIELD_PARRY;
        level.playSound(null, user.blockPosition(), breakSound, SoundSource.PLAYERS);

        if (level instanceof ServerLevel server && sourceEntity != null) {
            server.sendParticles(GuardingParticles.PARRY, sourceEntity.getX(), sourceEntity.getEyeY(), sourceEntity.getZ(), 1, 0.0d, 0.0d, 0.0d, 0.0d);
        }
    }

    // Handles the code for the Retribution Enchantment
    private static void onShieldDisabled(Player user, LivingEntity attacker) {
        ItemStack useItem = user.getUseItem();
        int retributionLevel = EnchantmentHelper.getItemEnchantmentLevel(GuardingEnchantments.RETRIBUTION, useItem);
        int amplifier = Guarding.CONFIG.getGroup("retribution").getIntProperty("slowness_amplifier");

        if (retributionLevel > 0) {
            List<LivingEntity> list = attacker.level().getEntitiesOfClass(LivingEntity.class, user.getBoundingBox().inflate(3.0d, 0.0d, 3.0d), livingEntity -> isEnemy(user, livingEntity));

            for (LivingEntity livingEntity : list) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, retributionLevel * 50, amplifier, true, true));
            }
        }
    }

    // The condition for retribution to apply
    private static boolean isEnemy(Player user, LivingEntity other) {
        if (other == user) return false;
        if (other instanceof Player otherPlayer) return user.canHarmPlayer(otherPlayer);
        return other.canBeSeenAsEnemy();
    }
}
