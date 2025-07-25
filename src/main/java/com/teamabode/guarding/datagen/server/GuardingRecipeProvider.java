package com.teamabode.guarding.datagen.server;

import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.common.recipe.SmithingTransformShieldRecipe;
import com.teamabode.guarding.common.recipe.SmithingTransformShieldRecipe.Builder;
import com.teamabode.guarding.core.registry.GuardingItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import java.util.concurrent.CompletableFuture;

public class GuardingRecipeProvider extends FabricRecipeProvider {
    public GuardingRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        var recipe = new SmithingTransformShieldRecipe.Builder(
                Ingredient.ofItems(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                Ingredient.ofItems(Items.SHIELD),
                Ingredient.ofItems(Items.NETHERITE_INGOT),
                RecipeCategory.TOOLS,
                GuardingItems.NETHERITE_SHIELD
        );
        recipe.unlocks("has_netherite_ingot", conditionsFromItem(Items.NETHERITE_INGOT));
        recipe.save(exporter, Guarding.id("netherite_shield_smithing"));
    }
}
