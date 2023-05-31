package com.teamabode.guarding.core.init;

import com.teamabode.guarding.Guarding;
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

import java.util.List;

public class GuardingEvents {

    public static void init() {
        com.teamabode.guarding.core.api.GuardingEvents.SHIELD_BLOCKED.register(GuardingEvents::onShieldBlock);
        com.teamabode.guarding.core.api.GuardingEvents.SHIELD_DISABLED.register(GuardingEvents::onShieldDisabled);
    }

    private static void onShieldBlock(Player user, DamageSource source, float amount) {
        Entity sourceEntity = source.getDirectEntity();
        boolean isParry = (user.getUseItem().getUseDuration() - user.getUseItemRemainingTicks()) <= 2;

        if (sourceEntity instanceof LivingEntity attacker) {
            if (isParry) {
                float exhaustion = Guarding.CONFIG.getGroup("parry").getFloatProperty("exhaustion_cost");
                float strength = getKnockbackStrength(user.getUseItem());

                user.causeFoodExhaustion(exhaustion);
                attacker.knockback(strength, user.getX() - attacker.getX(), user.getZ() - attacker.getZ());
                attacker.hurtMarked = true;
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

    private static float getKnockbackStrength(ItemStack stack) {
        float baseStrength = Guarding.CONFIG.getGroup("parry").getFloatProperty("knockback_strength");
        float additionalStrength = Guarding.CONFIG.getGroup("pummeling").getFloatProperty("additional_knockback_strength_per_level");
        int pummelingLevel = EnchantmentHelper.getItemEnchantmentLevel(GuardingEnchantments.PUMMELING, stack);
        float pummelStrength = pummelingLevel > 0 ? pummelingLevel * additionalStrength : 0.0f;

        return baseStrength + pummelStrength;
    }

    private static void parryEffects(Player user, Entity sourceEntity) {
        Level level = user.getLevel();
        SoundEvent breakSound = user.getUseItem().is(GuardingItems.NETHERITE_SHIELD) ? GuardingSounds.ITEM_NETHERITE_SHIELD_PARRY : GuardingSounds.ITEM_SHIELD_PARRY;
        level.playSound(null, user.blockPosition(), breakSound, SoundSource.PLAYERS);

        if (level instanceof ServerLevel server && sourceEntity != null) {
            server.sendParticles(GuardingParticles.PARRY, sourceEntity.getX(), sourceEntity.getEyeY(), sourceEntity.getZ(), 1, 0.0d, 0.0d, 0.0d, 0.0d);
        }
    }

    private static void onShieldDisabled(Player user, LivingEntity attacker) {
        ItemStack useItem = user.getUseItem();
        int retributionLevel = EnchantmentHelper.getItemEnchantmentLevel(GuardingEnchantments.RETRIBUTION, useItem);
        int amplifier = Guarding.CONFIG.getGroup("retribution").getIntProperty("slowness_amplifier");

        if (retributionLevel > 0) {
            List<LivingEntity> list = attacker.getLevel().getEntitiesOfClass(LivingEntity.class, user.getBoundingBox().inflate(3.0d, 0.0d, 3.0d), livingEntity -> isEnemy(user, livingEntity));

            for (LivingEntity livingEntity : list) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, retributionLevel * 50, amplifier, true, true));
            }
        }
    }

    private static boolean isEnemy(Player user, LivingEntity other) {
        if (other == user) return false;
        if (other instanceof Player otherPlayer) return user.canHarmPlayer(otherPlayer);
        return other.canBeSeenAsEnemy();
    }
}
