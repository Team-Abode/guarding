package com.teamabode.guarding.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.core.access.ProjectileAccessor;
import com.teamabode.guarding.core.registry.GuardingParticles;
import com.teamabode.guarding.core.util.EnchantmentUtils;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlocksAttacksComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.dynamic.Codecs;

import java.util.Optional;

public record ParriesAttacksComponent(float windowSeconds, float knockbackStrength, boolean spawnsParticle, Optional<RegistryEntry<SoundEvent>> sound, Optional<TagKey<DamageType>> bypassedBy) {
    public static final Codec<ParriesAttacksComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codecs.NON_NEGATIVE_FLOAT.fieldOf("window_seconds").forGetter(ParriesAttacksComponent::windowSeconds),
            Codecs.NON_NEGATIVE_FLOAT.fieldOf("knockback_strength").forGetter(ParriesAttacksComponent::knockbackStrength),
            Codec.BOOL.fieldOf("spawns_particle").forGetter(ParriesAttacksComponent::spawnsParticle),
            SoundEvent.ENTRY_CODEC.optionalFieldOf("sound").forGetter(ParriesAttacksComponent::sound),
            TagKey.codec(RegistryKeys.DAMAGE_TYPE).optionalFieldOf("bypassed_by").forGetter(ParriesAttacksComponent::bypassedBy)
    ).apply(instance, ParriesAttacksComponent::new));

    public static final PacketCodec<RegistryByteBuf, ParriesAttacksComponent> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.FLOAT, ParriesAttacksComponent::windowSeconds,
            PacketCodecs.FLOAT, ParriesAttacksComponent::knockbackStrength,
            PacketCodecs.BOOLEAN, ParriesAttacksComponent::spawnsParticle,
            SoundEvent.ENTRY_PACKET_CODEC.collect(PacketCodecs::optional), ParriesAttacksComponent::sound,
            TagKey.packetCodec(RegistryKeys.DAMAGE_TYPE).collect(PacketCodecs::optional), ParriesAttacksComponent::bypassedBy,
            ParriesAttacksComponent::new
    );

    public boolean canParry(LivingEntity entity, DamageSource source, ItemStack stack) {
        BlocksAttacksComponent blocksAttacksComponent = stack.get(DataComponentTypes.BLOCKS_ATTACKS);
        if (blocksAttacksComponent == null) return false;

        if (this.bypassedBy.isPresent() && source.isIn(this.bypassedBy.get())) return false;

        return entity.getItemUseTime() <= blocksAttacksComponent.getBlockDelayTicks() + this.getWindowTicks();
    }

    public void onParrySuccess(ServerWorld world, LivingEntity user, ItemStack stack, DamageSource source) {
        Entity sourceEntity = source.getSource();

        if (sourceEntity instanceof ProjectileEntity projectile) {
            if (projectile instanceof ProjectileAccessor accessor) {

                Guarding.LOGGER.info("Setting parrier to {}", user.getName().getLiteralString());

                accessor.setParrier(user);
            }

            projectile.setVelocity(projectile.getVelocity().multiply(7.5d));
            projectile.setYaw(projectile.getYaw() + 180.0f);

            projectile.velocityDirty = true;
        }
        else if (sourceEntity instanceof LivingEntity livingEntity) {
            float strength = EnchantmentUtils.modifyParryKnockback(stack, user, this.knockbackStrength);

            livingEntity.velocityModified = true;
            livingEntity.takeKnockback(strength, user.getX() - livingEntity.getX(), user.getZ() - livingEntity.getZ());
        }

        this.sound().ifPresent(entry -> world.playSound(
                null,
                user.getX(),
                user.getY(),
                user.getZ(),
                entry,
                user.getSoundCategory(),
                0.8F,
                0.8F + world.random.nextFloat() * 0.4F
        ));

        if (this.spawnsParticle() && sourceEntity != null) {
            Guarding.LOGGER.info("Spawning particle!");

            boolean isProjectile = sourceEntity instanceof ProjectileEntity;

            double x = isProjectile ? sourceEntity.getX() : (user.getX() + sourceEntity.getX()) / 2;
            double y = isProjectile ? sourceEntity.getY() : sourceEntity.getEyeY();
            double z = isProjectile ? sourceEntity.getZ() : (user.getZ() + sourceEntity.getZ()) / 2;

            world.spawnParticles(GuardingParticles.PARRY, x, y, z, 1, 0.0d, 0.0d, 0.0d, 0.0d);
        }

        EnchantmentUtils.runParriedEffects(world, user, stack, source);
    }

    public int getWindowTicks() {
        return Math.round(this.windowSeconds * 20.0f);
    }
}
