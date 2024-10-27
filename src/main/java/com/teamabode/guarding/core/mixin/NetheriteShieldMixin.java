package com.teamabode.guarding.core.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.teamabode.guarding.core.registry.GuardingItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class NetheriteShieldMixin extends LivingEntity {

    @Shadow public abstract ItemCooldowns getCooldowns();

    protected NetheriteShieldMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyExpressionValue(method = "hurtCurrentlyUsedShield", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
    private boolean guarding$hurtCurrentlyUsedShield(boolean original) {
        return original || useItem.is(GuardingItems.NETHERITE_SHIELD);
    }

    @Inject(method = "disableShield", at = @At("HEAD"))
    private void guarding$disableShield(CallbackInfo ci) {
        this.getCooldowns().addCooldown(GuardingItems.NETHERITE_SHIELD, 100);
        this.stopUsingItem();
        this.level().broadcastEntityEvent(this, (byte)30);
    }
}
