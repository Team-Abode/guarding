package com.teamabode.guarding.common.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabode.guarding.core.registry.GuardingRecipeSerializers;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.level.Level;

public record SmithingTransformShieldRecipe(Ingredient template, Ingredient base, Ingredient addition, ItemStack result) implements SmithingRecipe {
    public static final MapCodec<SmithingTransformShieldRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("template").forGetter(SmithingTransformShieldRecipe::template),
            Ingredient.CODEC.fieldOf("base").forGetter(SmithingTransformShieldRecipe::base),
            Ingredient.CODEC.fieldOf("addition").forGetter(SmithingTransformShieldRecipe::addition),
            ItemStack.STRICT_CODEC.fieldOf("result").forGetter(SmithingTransformShieldRecipe::result)
    ).apply(instance, SmithingTransformShieldRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SmithingTransformShieldRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

    @Override
    public boolean matches(SmithingRecipeInput input, Level level) {
        return this.template.test(input.template()) && this.base.test(input.base()) && this.addition.test(input.addition());
    }

    @Override
    public ItemStack assemble(SmithingRecipeInput input, HolderLookup.Provider provider) {
        ItemStack stack = input.base().transmuteCopy(result.getItem(), result.getCount());

        if (stack.has(DataComponents.BASE_COLOR)) {
            stack.remove(DataComponents.BASE_COLOR);
        }
        if (stack.has(DataComponents.BANNER_PATTERNS)) {
            stack.remove(DataComponents.BANNER_PATTERNS);
        }
        stack.applyComponents(result.getComponentsPatch());
        return stack;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.result;
    }

    @Override
    public boolean isTemplateIngredient(ItemStack stack) {
        return this.template.test(stack);
    }

    @Override
    public boolean isBaseIngredient(ItemStack stack) {
        return this.base.test(stack);
    }

    @Override
    public boolean isAdditionIngredient(ItemStack stack) {
        return this.addition.test(stack);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return GuardingRecipeSerializers.SMITHING_TRANSFORM_SHIELD;
    }

    @Override
    public boolean isIncomplete() {
        return Stream.of(this.template, this.base, this.addition).anyMatch(Ingredient::isEmpty);
    }

    public static class Serializer implements RecipeSerializer<SmithingTransformShieldRecipe> {
        @Override
        public MapCodec<SmithingTransformShieldRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SmithingTransformShieldRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        public static SmithingTransformShieldRecipe fromNetwork(RegistryFriendlyByteBuf packet) {
            Ingredient template = Ingredient.CONTENTS_STREAM_CODEC.decode(packet);
            Ingredient base = Ingredient.CONTENTS_STREAM_CODEC.decode(packet);
            Ingredient addition = Ingredient.CONTENTS_STREAM_CODEC.decode(packet);
            ItemStack result = ItemStack.STREAM_CODEC.decode(packet);
            return new SmithingTransformShieldRecipe(template, base, addition, result);
        }

        public static void toNetwork(RegistryFriendlyByteBuf packet, SmithingTransformShieldRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(packet, recipe.template);
            Ingredient.CONTENTS_STREAM_CODEC.encode(packet, recipe.base);
            Ingredient.CONTENTS_STREAM_CODEC.encode(packet, recipe.addition);
            ItemStack.STREAM_CODEC.encode(packet, recipe.result);
        }
    }

    public static class Builder {
        private final Ingredient template;
        private final Ingredient base;
        private final Ingredient addition;
        private final RecipeCategory category;
        private final Item result;
        private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

        public Builder(Ingredient template, Ingredient base, Ingredient addition, RecipeCategory category, Item result) {
            this.template = template;
            this.base = base;
            this.addition = addition;
            this.category = category;
            this.result = result;
        }

        public Builder unlocks(String string, Criterion<?> criterion) {
            this.criteria.put(string, criterion);
            return this;
        }

        public void save(RecipeOutput exporter, ResourceLocation resourceLocation) {
            this.ensureValid(resourceLocation);
            Advancement.Builder advancement = exporter.advancement()
                    .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceLocation))
                    .rewards(AdvancementRewards.Builder.recipe(resourceLocation))
                    .requirements(AdvancementRequirements.Strategy.OR);
            this.criteria.forEach(advancement::addCriterion);

            SmithingTransformShieldRecipe recipe = new SmithingTransformShieldRecipe(this.template, this.base, this.addition, new ItemStack(this.result));
            exporter.accept(resourceLocation, recipe, advancement.build(resourceLocation.withPrefix("recipes/" + this.category.getFolderName() + "/")));
        }

        private void ensureValid(ResourceLocation resourceLocation) {
            if (this.criteria.isEmpty()) {
                throw new IllegalStateException("No way of obtaining recipe " + resourceLocation);
            }
        }
    }
}
