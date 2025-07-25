package com.teamabode.guarding.common.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabode.guarding.core.registry.GuardingRecipeSerializers;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.recipe.input.SmithingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public record SmithingTransformShieldRecipe(Ingredient template, Ingredient base, Ingredient addition, ItemStack result) implements SmithingRecipe {
    public static final MapCodec<SmithingTransformShieldRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.ALLOW_EMPTY_CODEC.fieldOf("template").forGetter(SmithingTransformShieldRecipe::template),
            Ingredient.ALLOW_EMPTY_CODEC.fieldOf("base").forGetter(SmithingTransformShieldRecipe::base),
            Ingredient.ALLOW_EMPTY_CODEC.fieldOf("addition").forGetter(SmithingTransformShieldRecipe::addition),
            ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(SmithingTransformShieldRecipe::result)
    ).apply(instance, SmithingTransformShieldRecipe::new));

    public static final PacketCodec<RegistryByteBuf, SmithingTransformShieldRecipe> PACKET_CODEC = PacketCodec.ofStatic(Serializer::toNetwork, Serializer::fromNetwork);

    @Override
    public boolean matches(SmithingRecipeInput input, World level) {
        return this.template.test(input.template()) && this.base.test(input.base()) && this.addition.test(input.addition());
    }

    @Override
    public ItemStack craft(SmithingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        ItemStack stack = input.base().copyComponentsToNewStack(result.getItem(), result.getCount());

        if (stack.contains(DataComponentTypes.BASE_COLOR)) {
            stack.remove(DataComponentTypes.BASE_COLOR);
        }
        if (stack.contains(DataComponentTypes.BANNER_PATTERNS)) {
            stack.remove(DataComponentTypes.BANNER_PATTERNS);
        }
        stack.applyUnvalidatedChanges(result.getComponentChanges());
        return stack;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup provider) {
        return this.result;
    }

    @Override
    public boolean testTemplate(ItemStack stack) {
        return this.template.test(stack);
    }

    @Override
    public boolean testBase(ItemStack stack) {
        return this.base.test(stack);
    }

    @Override
    public boolean testAddition(ItemStack stack) {
        return this.addition.test(stack);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return GuardingRecipeSerializers.SMITHING_TRANSFORM_SHIELD;
    }

    @Override
    public boolean isEmpty() {
        return Stream.of(this.template, this.base, this.addition).anyMatch(Ingredient::isEmpty);
    }

    public static class Serializer implements RecipeSerializer<SmithingTransformShieldRecipe> {
        @Override
        public MapCodec<SmithingTransformShieldRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, SmithingTransformShieldRecipe> packetCodec() {
            return PACKET_CODEC;
        }

        public static SmithingTransformShieldRecipe fromNetwork(RegistryByteBuf packet) {
            Ingredient template = Ingredient.PACKET_CODEC.decode(packet);
            Ingredient base = Ingredient.PACKET_CODEC.decode(packet);
            Ingredient addition = Ingredient.PACKET_CODEC.decode(packet);
            ItemStack result = ItemStack.PACKET_CODEC.decode(packet);
            return new SmithingTransformShieldRecipe(template, base, addition, result);
        }

        public static void toNetwork(RegistryByteBuf packet, SmithingTransformShieldRecipe recipe) {
            Ingredient.PACKET_CODEC.encode(packet, recipe.template);
            Ingredient.PACKET_CODEC.encode(packet, recipe.base);
            Ingredient.PACKET_CODEC.encode(packet, recipe.addition);
            ItemStack.PACKET_CODEC.encode(packet, recipe.result);
        }
    }

    public static class Builder {
        private final Ingredient template;
        private final Ingredient base;
        private final Ingredient addition;
        private final RecipeCategory category;
        private final Item result;
        private final Map<String, AdvancementCriterion<?>> criteria = new LinkedHashMap<>();

        public Builder(Ingredient template, Ingredient base, Ingredient addition, RecipeCategory category, Item result) {
            this.template = template;
            this.base = base;
            this.addition = addition;
            this.category = category;
            this.result = result;
        }

        public Builder unlocks(String string, AdvancementCriterion<?> criterion) {
            this.criteria.put(string, criterion);
            return this;
        }

        public void save(RecipeExporter exporter, Identifier resourceLocation) {
            this.ensureValid(resourceLocation);
            Advancement.Builder advancement = exporter.getAdvancementBuilder()
                    .criterion("has_the_recipe", RecipeUnlockedCriterion.create(resourceLocation))
                    .rewards(AdvancementRewards.Builder.recipe(resourceLocation))
                    .criteriaMerger(AdvancementRequirements.CriterionMerger.OR);
            this.criteria.forEach(advancement::criterion);

            SmithingTransformShieldRecipe recipe = new SmithingTransformShieldRecipe(this.template, this.base, this.addition, new ItemStack(this.result));
            exporter.accept(resourceLocation, recipe, advancement.build(resourceLocation.withPrefixedPath("recipes/" + this.category.getName() + "/")));
        }

        private void ensureValid(Identifier resourceLocation) {
            if (this.criteria.isEmpty()) {
                throw new IllegalStateException("No way of obtaining recipe " + resourceLocation);
            }
        }
    }
}
