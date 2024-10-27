package com.teamabode.guarding.datagen.server;

import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.common.critieria.KilledByParriedArrowCriterion;
import com.teamabode.guarding.core.registry.GuardingCriterions;
import com.teamabode.guarding.core.registry.GuardingItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.RecipeCraftedCriterion;
import net.minecraft.block.entity.BannerPatterns;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class GuardingAdvancementProvider extends FabricAdvancementProvider {
    public static final Identifier ON_GUARD = Guarding.id("nether/on_guard");
    public static final Identifier PARRY_THIS_YOU_CASUAL = Guarding.id("story/parry_this_you_casual");
    public static final Identifier SYMBOLIC_SHIELD = Guarding.id("story/symbolic_shield");

    public GuardingAdvancementProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(RegistryWrapper.WrapperLookup lookup, Consumer<AdvancementEntry> exporter) {
        createOnGuard(exporter);
        createParryThisYouCasual(exporter);
        createSymbolicShield(lookup, exporter);
    }

    private static void createOnGuard(Consumer<AdvancementEntry> exporter) {
        var advancement = Advancement.Builder.create();
        advancement.display(
                GuardingItems.NETHERITE_SHIELD,
                Text.translatable("advancements.guarding.nether.on_guard"),
                Text.translatable("advancements.guarding.nether.on_guard.desc"),
                null,
                AdvancementFrame.CHALLENGE,
                true, true, false
        );
        advancement.criterion("netherite_shield", InventoryChangedCriterion.Conditions.items(GuardingItems.NETHERITE_SHIELD));
        advancement.parent(new AdvancementEntry(Identifier.ofVanilla("nether/netherite_armor"), null));
        advancement.criteriaMerger(AdvancementRequirements.CriterionMerger.AND);
        advancement.build(exporter, ON_GUARD.toString());
    }

    private static void createParryThisYouCasual(Consumer<AdvancementEntry> exporter) {
        var advancement = Advancement.Builder.create();
        advancement.display(
                Items.SHIELD,
                Text.translatable("advancements.guarding.story.parry_this_you_casual"),
                Text.translatable("advancements.guarding.story.parry_this_you_casual.desc"),
                null,
                AdvancementFrame.CHALLENGE,
                true, true, true
        );
        advancement.criterion("killed_by_parried_arrow", new AdvancementCriterion<>(
                GuardingCriterions.KILLED_BY_PARRIED_ARROW,
                new KilledByParriedArrowCriterion.TriggerInstance(Optional.empty(), Optional.empty())
        ));
        advancement.parent(new AdvancementEntry(SYMBOLIC_SHIELD, null));
        advancement.criteriaMerger(AdvancementRequirements.CriterionMerger.AND);
        advancement.build(exporter, PARRY_THIS_YOU_CASUAL.toString());
    }

    private static void createSymbolicShield(RegistryWrapper.WrapperLookup lookup, Consumer<AdvancementEntry> exporter) {
        var advancement = Advancement.Builder.create();
        var bannerPatterns = lookup.getWrapperOrThrow(RegistryKeys.BANNER_PATTERN);

        ItemStack displayStack = new ItemStack(Items.SHIELD);
        displayStack.set(DataComponentTypes.BASE_COLOR, DyeColor.BLUE);
        displayStack.set(DataComponentTypes.BANNER_PATTERNS, new BannerPatternsComponent.Builder().add(bannerPatterns.getOrThrow(BannerPatterns.STRIPE_CENTER), DyeColor.YELLOW).build());
        displayStack.set(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);

        advancement.display(
                displayStack,
                Text.translatable("advancements.guarding.story.symbolic_shield"),
                Text.translatable("advancements.guarding.story.symbolic_shield.desc"),
                null,
                AdvancementFrame.TASK,
                true, true, false
        );
        advancement.criterion("decorate_shield", RecipeCraftedCriterion.Conditions.create(Identifier.ofVanilla("shield_decoration")));
        advancement.parent(new AdvancementEntry(Identifier.ofVanilla("story/deflect_arrow"), null));
        advancement.criteriaMerger(AdvancementRequirements.CriterionMerger.AND);
        advancement.build(exporter, SYMBOLIC_SHIELD.toString());
    }
}
