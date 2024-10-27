package com.teamabode.guarding.core.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.teamabode.guarding.core.registry.GuardingItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class NetheriteShieldMixin extends LivingEntity {
    @Shadow public abstract ItemCooldownManager getItemCooldownManager();

    protected NetheriteShieldMixin(EntityType<? extends LivingEntity> entityType, World level) {
        super(entityType, level);
    }

    @ModifyExpressionValue(method = "damageShield", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private boolean guarding$hurtCurrentlyUsedShield(boolean original) {
        return original || activeItemStack.isOf(GuardingItems.NETHERITE_SHIELD);
    }

    @Inject(method = "disableShield", at = @At("HEAD"))
    private void guarding$disableShield(CallbackInfo ci) {
        this.getItemCooldownManager().set(GuardingItems.NETHERITE_SHIELD, 100);
        this.clearActiveItem();
        this.getWorld().sendEntityStatus(this, (byte)30);
    }
}
