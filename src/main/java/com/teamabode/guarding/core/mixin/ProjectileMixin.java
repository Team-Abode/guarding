package com.teamabode.guarding.core.mixin;

import com.teamabode.guarding.core.access.ProjectileAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;

@Mixin(Projectile.class)
public class ProjectileMixin implements ProjectileAccessor {
    private UUID parrierUUID;
    private Entity cachedParrier;

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void guarding$readCustomDataFromNbt(CompoundTag nbt, CallbackInfo ci) {
        if (nbt.hasUUID("Parrier")) {
            this.parrierUUID = nbt.getUUID("Parrier");
            this.cachedParrier = null;
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void guarding$writeCustomDataToNbt(CompoundTag nbt, CallbackInfo ci) {
        if (this.parrierUUID != null) {
            nbt.putUUID("Parrier", this.parrierUUID);
        }
    }

    @SuppressWarnings("all")
    public Entity getParrier() {
        final Projectile $this = Projectile.class.cast(this);

        if (this.cachedParrier != null && !this.cachedParrier.isRemoved()) {
            return this.cachedParrier;
        }
        if (this.parrierUUID != null && $this.level() instanceof ServerLevel server) {
            this.cachedParrier = server.getEntity(this.parrierUUID);
            return this.cachedParrier;
        }
        return null;
    }

    @SuppressWarnings("all")
    public void setParrier(Entity parrier) {
        if (parrier != null) {
            this.parrierUUID = parrier.getUUID();
            this.cachedParrier = parrier;
        }
    }
}
