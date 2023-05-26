package com.teamabode.guarding.core.mixin.gameplay;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(LivingEntity.class)
public class ShieldDelayMixin {

    @ModifyConstant(method = "isBlocking", constant = @Constant(intValue = 5))
    private int removeShieldDelay(int useDelay) {
        useDelay = 0;
        return useDelay;
    }
}
