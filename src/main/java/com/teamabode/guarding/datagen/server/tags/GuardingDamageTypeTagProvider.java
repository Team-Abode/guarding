package com.teamabode.guarding.datagen.server.tags;

import com.teamabode.guarding.core.tag.GuardingDamageTypeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import java.util.concurrent.CompletableFuture;

public class GuardingDamageTypeTagProvider extends FabricTagProvider<DamageType> {
    public GuardingDamageTypeTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.DAMAGE_TYPE, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        this.getOrCreateTagBuilder(GuardingDamageTypeTags.NO_PARRY)
                .forceAddTag(DamageTypeTags.IS_EXPLOSION)
                .forceAddTag(DamageTypeTags.BYPASSES_SHIELD);
    }
}
