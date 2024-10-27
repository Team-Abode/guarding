package com.teamabode.guarding.core.mixin;

import com.teamabode.guarding.core.registry.GuardingItems;
import com.teamabode.guarding.core.registry.GuardingSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class NetheriteShieldSoundMixin {

    @Shadow public abstract ItemStack getUseItem();

    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;broadcastEntityEvent(Lnet/minecraft/world/entity/Entity;B)V"))
    private void guarding$hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (this.getUseItem().is(GuardingItems.NETHERITE_SHIELD)) {
            LivingEntity livingEntity = LivingEntity.class.cast(this);
            livingEntity.level().playSound(null, livingEntity.blockPosition(), GuardingSounds.ITEM_NETHERITE_SHIELD_BLOCK, SoundSource.NEUTRAL, 1.0f, 1.0f);
        }
    }
}
