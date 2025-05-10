package com.teamabode.guarding.datagen.server;

import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.common.recipe.SmithingTransformShieldRecipe;
import com.teamabode.guarding.common.recipe.SmithingTransformShieldRecipe.Builder;
import com.teamabode.guarding.core.registry.GuardingItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import java.util.concurrent.CompletableFuture;

public class GuardingRecipeProvider extends FabricRecipeProvider {
    public GuardingRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput exporter) {
        var recipe = new SmithingTransformShieldRecipe.Builder(
                Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                Ingredient.of(Items.SHIELD),
                Ingredient.of(Items.NETHERITE_INGOT),
                RecipeCategory.TOOLS,
                GuardingItems.NETHERITE_SHIELD
        );
        recipe.unlocks("has_netherite_ingot", has(Items.NETHERITE_INGOT));
        recipe.save(exporter, Guarding.id("netherite_shield_smithing"));
    }
}
