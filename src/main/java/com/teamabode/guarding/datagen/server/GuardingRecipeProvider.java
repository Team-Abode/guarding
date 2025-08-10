package com.teamabode.guarding.datagen.server;

import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.core.registry.GuardingItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.SmithingTransformRecipe;
import net.minecraft.recipe.TransmuteRecipeResult;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class GuardingRecipeProvider extends FabricRecipeProvider {
    public GuardingRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
        return new GuardingRecipeGenerator(registries, exporter);
    }

    @Override
    public String getName() {
        return "Recipes";
    }

    public static RegistryKey<Recipe<?>> keyOf(Identifier id) {
        return RegistryKey.of(RegistryKeys.RECIPE, id);
    }

    public static class GuardingRecipeGenerator extends RecipeGenerator {

        public GuardingRecipeGenerator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
            super(registries, exporter);
        }

        @Override
        public void generate() {
            var key = keyOf(Guarding.id("netherite_shield_smithing"));

            var result = new TransmuteRecipeResult(
                    GuardingItems.NETHERITE_SHIELD.getRegistryEntry(),
                    1,
                    ComponentChanges.builder()
                            .remove(DataComponentTypes.BASE_COLOR)
                            .remove(DataComponentTypes.BANNER_PATTERNS)
                            .build()
            );

            var recipe = new SmithingTransformRecipe(
                    Optional.of(Ingredient.ofItems(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)),
                    Ingredient.ofItems(Items.SHIELD),
                    Optional.of(Ingredient.ofItems(Items.NETHERITE_INGOT)),
                    result
            );

            var advancement = exporter.getAdvancementBuilder()
                    .criterion("has_the_recipe", RecipeUnlockedCriterion.create(key))
                    .rewards(AdvancementRewards.Builder.recipe(key))
                    .criteriaMerger(AdvancementRequirements.CriterionMerger.OR)
                    .build(Guarding.id("recipes/combat/netherite_shield_smithing"));

            exporter.accept(key, recipe, advancement);
        }
    }
}
