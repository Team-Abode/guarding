package com.teamabode.guarding.datagen.client;

import com.teamabode.guarding.client.render.NetheriteShieldRenderer;
import com.teamabode.guarding.core.registry.GuardingItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.*;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.item.Items;

public class GuardingModelProvider extends FabricModelProvider {

    public GuardingModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        // Netherite Shield
        ItemModel.Unbaked model = ItemModels.special(ModelIds.getItemModelId(Items.SHIELD), new NetheriteShieldRenderer.Unbaked());
        ItemModel.Unbaked blockingModel = ItemModels.special(ModelIds.getItemSubModelId(Items.SHIELD, "_blocking"), new NetheriteShieldRenderer.Unbaked());

        generator.registerCondition(GuardingItems.NETHERITE_SHIELD, ItemModels.usingItemProperty(), blockingModel, model);
    }
}
