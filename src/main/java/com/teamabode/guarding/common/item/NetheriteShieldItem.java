package com.teamabode.guarding.common.item;

import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.core.init.GuardingSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class NetheriteShieldItem extends ShieldItem {
    private static final UUID SHIELDING_MODIFIER_UUID = UUID.fromString("8b128327-f878-4e94-ada2-707cd81b13af");
    private static final AttributeModifier SHIELDING_MODIFIER = new AttributeModifier(SHIELDING_MODIFIER_UUID, "Shielding knockback resistance", 0.1d, AttributeModifier.Operation.ADDITION);

    public NetheriteShieldItem(Properties properties) {
        super(properties);
    }

    public int getEnchantmentValue() {
        return 15;
    }

    public SoundEvent getEquipSound() {
        return GuardingSounds.ITEM_NETHERITE_SHIELD_EQUIP;
    }

    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        AttributeInstance instance = livingEntity.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
        if (instance != null && instance.getModifier(SHIELDING_MODIFIER_UUID) == null) {
            instance.addTransientModifier(SHIELDING_MODIFIER);
        }
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> toolTips, TooltipFlag isAdvanced) {
        toolTips.add(Component.empty());
        toolTips.add(Component.translatable("item.modifiers.shielding").withStyle(ChatFormatting.GRAY));
        toolTips.add(Component.translatable("item.modifiers.netherite_shield").withStyle(ChatFormatting.BLUE));
    }

    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        AttributeInstance instance = livingEntity.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
        if (instance != null && instance.getModifier(SHIELDING_MODIFIER_UUID) != null) {
            instance.removeModifier(SHIELDING_MODIFIER_UUID);
        }
    }

    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return repairCandidate.is(Items.NETHERITE_INGOT) || super.isValidRepairItem(stack, repairCandidate);
    }

    public boolean isEnabled(FeatureFlagSet enabledFeatures) {
        return Guarding.CONFIG.getGroup("general").getBooleanProperty("netherite_shield_enabled");
    }
}
