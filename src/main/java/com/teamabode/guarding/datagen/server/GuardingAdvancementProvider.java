package com.teamabode.guarding.datagen.server;

import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.common.critieria.KilledByParriedArrowCriterion;
import com.teamabode.guarding.core.registry.GuardingCriterions;
import com.teamabode.guarding.core.registry.GuardingItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.Advancement.Builder;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.RecipeCraftedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.RegistryLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.entity.BannerPatterns;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class GuardingAdvancementProvider extends FabricAdvancementProvider {
    public static final ResourceLocation ON_GUARD = Guarding.id("nether/on_guard");
    public static final ResourceLocation PARRY_THIS_YOU_CASUAL = Guarding.id("story/parry_this_you_casual");
    public static final ResourceLocation SYMBOLIC_SHIELD = Guarding.id("story/symbolic_shield");

    public GuardingAdvancementProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(HolderLookup.Provider lookup, Consumer<AdvancementHolder> exporter) {
        createOnGuard(exporter);
        createParryThisYouCasual(exporter);
        createSymbolicShield(lookup, exporter);
    }

    private static void createOnGuard(Consumer<AdvancementHolder> exporter) {
        var advancement = Advancement.Builder.advancement();
        advancement.display(
                GuardingItems.NETHERITE_SHIELD,
                Component.translatable("advancements.guarding.nether.on_guard"),
                Component.translatable("advancements.guarding.nether.on_guard.desc"),
                null,
                AdvancementType.CHALLENGE,
                true, true, false
        );
        advancement.addCriterion("netherite_shield", InventoryChangeTrigger.TriggerInstance.hasItems(GuardingItems.NETHERITE_SHIELD));
        advancement.parent(new AdvancementHolder(ResourceLocation.withDefaultNamespace("nether/netherite_armor"), null));
        advancement.requirements(AdvancementRequirements.Strategy.AND);
        advancement.save(exporter, ON_GUARD.toString());
    }

    private static void createParryThisYouCasual(Consumer<AdvancementHolder> exporter) {
        var advancement = Advancement.Builder.advancement();
        advancement.display(
                Items.SHIELD,
                Component.translatable("advancements.guarding.story.parry_this_you_casual"),
                Component.translatable("advancements.guarding.story.parry_this_you_casual.desc"),
                null,
                AdvancementType.CHALLENGE,
                true, true, true
        );
        advancement.addCriterion("killed_by_parried_arrow", new Criterion<>(
                GuardingCriterions.KILLED_BY_PARRIED_ARROW,
                new KilledByParriedArrowCriterion.TriggerInstance(Optional.empty(), Optional.empty())
        ));
        advancement.parent(new AdvancementHolder(SYMBOLIC_SHIELD, null));
        advancement.requirements(AdvancementRequirements.Strategy.AND);
        advancement.save(exporter, PARRY_THIS_YOU_CASUAL.toString());
    }

    private static void createSymbolicShield(HolderLookup.Provider lookup, Consumer<AdvancementHolder> exporter) {
        var advancement = Advancement.Builder.advancement();
        var bannerPatterns = lookup.lookupOrThrow(Registries.BANNER_PATTERN);

        ItemStack displayStack = new ItemStack(Items.SHIELD);
        displayStack.set(DataComponents.BASE_COLOR, DyeColor.BLUE);
        displayStack.set(DataComponents.BANNER_PATTERNS, new BannerPatternLayers.Builder().add(bannerPatterns.getOrThrow(BannerPatterns.STRIPE_CENTER), DyeColor.YELLOW).build());
        displayStack.set(DataComponents.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);

        advancement.display(
                displayStack,
                Component.translatable("advancements.guarding.story.symbolic_shield"),
                Component.translatable("advancements.guarding.story.symbolic_shield.desc"),
                null,
                AdvancementType.TASK,
                true, true, false
        );
        advancement.addCriterion("decorate_shield", RecipeCraftedTrigger.TriggerInstance.craftedItem(ResourceLocation.withDefaultNamespace("shield_decoration")));
        advancement.parent(new AdvancementHolder(ResourceLocation.withDefaultNamespace("story/deflect_arrow"), null));
        advancement.requirements(AdvancementRequirements.Strategy.AND);
        advancement.save(exporter, SYMBOLIC_SHIELD.toString());
    }
}
