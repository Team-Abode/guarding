package com.teamabode.guarding.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;

/**
 * This mixin is used to add UI icons for Shields in the Smithing Table menu
 */
@Mixin(SmithingTemplateItem.class)
public class SmithingTemplateItemMixin {
    @Unique
    private static final ResourceLocation EMPTY_SLOT_SHIELD = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_shield");

    @Inject(method = "createTrimmableArmorIconList", at = @At("RETURN"), cancellable = true)
    private static void guarding$getArmorTrimEmptyBaseSlotTextures(CallbackInfoReturnable<List<ResourceLocation>> cir) {
        ArrayList<ResourceLocation> icons = new ArrayList<>(cir.getReturnValue());
        icons.add(EMPTY_SLOT_SHIELD);
        cir.setReturnValue(icons);
    }

    @Inject(method = "createNetheriteUpgradeIconList", at = @At("RETURN"), cancellable = true)
    private static void guarding$getNetheriteUpgradeEmptyBaseSlotTextures(CallbackInfoReturnable<List<ResourceLocation>> cir) {
        ArrayList<ResourceLocation> icons = new ArrayList<>(cir.getReturnValue());
        icons.add(EMPTY_SLOT_SHIELD);
        cir.setReturnValue(icons);
    }
}
