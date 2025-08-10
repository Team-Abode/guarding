package com.teamabode.guarding.datagen.server.tags;

import com.teamabode.guarding.core.tag.GuardingDamageTypeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.DamageTypeTags;
import java.util.concurrent.CompletableFuture;

public class GuardingDamageTypeTagProvider extends FabricTagProvider<DamageType> {
    public GuardingDamageTypeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.DAMAGE_TYPE, registriesFuture);
    }

    @SuppressWarnings("all")
    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        this.builder(GuardingDamageTypeTags.CANNOT_PARRY)
                .forceAddTag(DamageTypeTags.IS_EXPLOSION)
                .forceAddTag(DamageTypeTags.BYPASSES_SHIELD);
    }
}
