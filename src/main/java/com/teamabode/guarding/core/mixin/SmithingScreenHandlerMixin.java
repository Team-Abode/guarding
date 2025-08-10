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
import net.minecraft.screen.slot.ForgingSlotsManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DyeColor;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.xml.crypto.Data;

/**
 * This mixin is used to refund banners
 */
@Mixin(SmithingScreenHandler.class)
public abstract class SmithingScreenHandlerMixin extends ForgingScreenHandler {

    @Shadow @Final private World world;

    public SmithingScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, ForgingSlotsManager forgingSlotsManager) {
        super(type, syncId, playerInventory, context, forgingSlotsManager);
    }

    @Inject(method = "onTakeOutput", at = @At("HEAD"))
    private void guarding$onTakeOutput(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        ItemStack baseStack = input.getStack(1);

        DyeColor baseColor = baseStack.get(DataComponentTypes.BASE_COLOR);
        if (baseColor == null) return;

        Block banner = BannerBlock.getForColor(baseColor);
        ItemStack bannerStack = new ItemStack(banner);

        if (baseStack.contains(DataComponentTypes.BANNER_PATTERNS)) {
            bannerStack.set(DataComponentTypes.BANNER_PATTERNS, baseStack.get(DataComponentTypes.BANNER_PATTERNS));
        }

        if (!player.giveItemStack(bannerStack) && world instanceof ServerWorld server) {
            player.dropStack(server, stack);
        }
    }
}
