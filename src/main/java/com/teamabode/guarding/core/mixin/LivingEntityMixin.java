package com.teamabode.guarding.core.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.teamabode.guarding.common.component.ParriesAttacksComponent;
import com.teamabode.guarding.core.registry.GuardingDataComponentTypes;
import com.teamabode.guarding.core.util.EnchantmentUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Unique
    private final LivingEntity $this = LivingEntity.class.cast(this);

    @Inject(
            method = "getDamageBlockedAmount",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/component/type/BlocksAttacksComponent;onShieldHit(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/Hand;F)V",
                    shift = At.Shift.AFTER
            )
    )
    private void guarding$getDamageBlockedAmount(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Float> cir, @Local ItemStack stack) {
        ParriesAttacksComponent parriesAttacksComponent = stack.get(GuardingDataComponentTypes.PARRIES_ATTACKS);

        if (parriesAttacksComponent == null) {
            EnchantmentUtils.runBlockedEffects(world, $this, stack, source, false);
            return;
        }

        boolean performedParry = parriesAttacksComponent.canParry($this, source, stack);
        EnchantmentUtils.runBlockedEffects(world, $this, stack, source, performedParry);

        if (performedParry) {
            parriesAttacksComponent.onParrySuccess(world, $this, stack, source);
        }
    }
}
