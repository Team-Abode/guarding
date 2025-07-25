package com.teamabode.guarding.core.registry;

import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.common.recipe.SmithingTransformShieldRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class GuardingRecipeSerializers {

    public static final RecipeSerializer<SmithingTransformShieldRecipe> SMITHING_TRANSFORM_SHIELD = register("smithing_transform_shield", new SmithingTransformShieldRecipe.Serializer());

    private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String name, S serializer) {
        return Registry.register(Registries.RECIPE_SERIALIZER, Guarding.id(name), serializer);
    }

    public static void init() {
    }
}
