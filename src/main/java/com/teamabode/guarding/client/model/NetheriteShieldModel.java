package com.teamabode.guarding.client.model;

import com.teamabode.guarding.Guarding;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;

public class NetheriteShieldModel extends ShieldModel {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(Guarding.MOD_ID, "netherite_shield"), "main");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Guarding.MOD_ID, "textures/entity/netherite_shield.png");

    public NetheriteShieldModel(ModelPart modelPart) {
        super(modelPart);
    }
}
