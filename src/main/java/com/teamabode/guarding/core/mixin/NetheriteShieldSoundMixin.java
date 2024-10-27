package com.teamabode.guarding.core.mixin;

import com.teamabode.guarding.core.registry.GuardingItems;
import com.teamabode.guarding.core.registry.GuardingSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class NetheriteShieldSoundMixin {
    @Shadow public abstract ItemStack getActiveItem();

    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;sendEntityStatus(Lnet/minecraft/entity/Entity;B)V"))
    private void guarding$damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (this.getActiveItem().isOf(GuardingItems.NETHERITE_SHIELD)) {
            LivingEntity livingEntity = LivingEntity.class.cast(this);
            livingEntity.getWorld().playSound(null, livingEntity.getBlockPos(), GuardingSounds.ITEM_NETHERITE_SHIELD_BLOCK, SoundCategory.NEUTRAL, 1.0f, 1.0f);
        }
    }
}
