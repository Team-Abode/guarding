package com.teamabode.guarding.core.mixin.gameplay;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmithingMenu.class)
public abstract class BannerRefundMixin extends ItemCombinerMenu {

    public BannerRefundMixin(@Nullable MenuType<?> menuType, int i, Inventory inventory, ContainerLevelAccess containerLevelAccess) {
        super(menuType, i, inventory, containerLevelAccess);
    }

    @Inject(method = "onTake", at = @At("HEAD"))
    private void refundBanner(Player player, ItemStack stack, CallbackInfo ci) {
        ItemStack baseStack = inputSlots.getItem(1);
        CompoundTag tag = BlockItem.getBlockEntityData(baseStack);

        if (tag != null) {
            int colorID = tag.contains("Base") ? tag.getInt("Base") : 0;
            Block banner = BannerBlock.byColor(DyeColor.byId(colorID));
            ItemStack bannerStack = new ItemStack(banner);
            BlockItem.setBlockEntityData(bannerStack, BlockEntityType.BANNER, tag);
            player.addItem(bannerStack);
        }
    }
}
