package com.teamabode.guarding.core.registry;

import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.common.recipe.SmithingTransformShieldRecipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class GuardingRecipeSerializers {

    public static final RecipeSerializer<SmithingTransformShieldRecipe> SMITHING_TRANSFORM_SHIELD = register("smithing_transform_shield", new SmithingTransformShieldRecipe.Serializer());

    private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String name, S serializer) {
        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Guarding.id(name), serializer);
    }

    public static void init() {
    }
}
