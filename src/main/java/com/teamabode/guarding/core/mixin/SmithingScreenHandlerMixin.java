package com.teamabode.guarding.core.mixin;

import net.minecraft.block.BannerBlock;
import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.util.DyeColor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This mixin is used to refund banners
 */
@Mixin(SmithingScreenHandler.class)
public abstract class SmithingScreenHandlerMixin extends ForgingScreenHandler {

    public SmithingScreenHandlerMixin(@Nullable ScreenHandlerType<?> menuType, int i, PlayerInventory inventory, ScreenHandlerContext containerLevelAccess) {
        super(menuType, i, inventory, containerLevelAccess);
    }

    @Inject(method = "onTakeOutput", at = @At("HEAD"))
    private void guarding$onTakeOutput(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        ItemStack baseStack = input.getStack(1);
        if (!baseStack.contains(DataComponentTypes.BASE_COLOR)) {
            return;
        }
        int colorID = baseStack.get(DataComponentTypes.BASE_COLOR).getId();
        Block banner = BannerBlock.getForColor(DyeColor.byId(colorID));
        ItemStack bannerStack = new ItemStack(banner);

        if (baseStack.contains(DataComponentTypes.BANNER_PATTERNS)) {
            bannerStack.set(DataComponentTypes.BANNER_PATTERNS, baseStack.get(DataComponentTypes.BANNER_PATTERNS));
        }
        if (!player.giveItemStack(bannerStack)) {
            player.dropStack(bannerStack);
        }
    }
}
