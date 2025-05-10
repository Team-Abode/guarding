package com.teamabode.guarding.core.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This mixin is used to refund banners
 */
@Mixin(SmithingMenu.class)
public abstract class SmithingScreenHandlerMixin extends ItemCombinerMenu {

    public SmithingScreenHandlerMixin(@Nullable MenuType<?> menuType, int i, Inventory inventory, ContainerLevelAccess containerLevelAccess) {
        super(menuType, i, inventory, containerLevelAccess);
    }

    @Inject(method = "onTake", at = @At("HEAD"))
    private void guarding$onTake(Player player, ItemStack stack, CallbackInfo ci) {
        ItemStack baseStack = inputSlots.getItem(1);
        if (!baseStack.has(DataComponents.BASE_COLOR)) {
            return;
        }
        int colorID = baseStack.get(DataComponents.BASE_COLOR).getId();
        Block banner = BannerBlock.byColor(DyeColor.byId(colorID));
        ItemStack bannerStack = new ItemStack(banner);

        if (baseStack.has(DataComponents.BANNER_PATTERNS)) {
            bannerStack.set(DataComponents.BANNER_PATTERNS, baseStack.get(DataComponents.BANNER_PATTERNS));
        }
        if (!player.addItem(bannerStack)) {
            player.spawnAtLocation(bannerStack);
        }
    }
}
