package com.teamabode.guarding.core.mixin;

import com.teamabode.guarding.core.access.ProjectileAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;

@Mixin(ProjectileEntity.class)
public class ProjectileMixin implements ProjectileAccessor {
    private UUID parrierUUID;
    private Entity cachedParrier;

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void guarding$readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.containsUuid("Parrier")) {
            this.parrierUUID = nbt.getUuid("Parrier");
            this.cachedParrier = null;
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void guarding$writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        if (this.parrierUUID != null) {
            nbt.putUuid("Parrier", this.parrierUUID);
        }
    }

    @SuppressWarnings("all")
    public Entity getParrier() {
        final ProjectileEntity $this = ProjectileEntity.class.cast(this);

        if (this.cachedParrier != null && !this.cachedParrier.isRemoved()) {
            return this.cachedParrier;
        }
        if (this.parrierUUID != null && $this.getWorld() instanceof ServerWorld server) {
            this.cachedParrier = server.getEntity(this.parrierUUID);
            return this.cachedParrier;
        }
        return null;
    }

    @SuppressWarnings("all")
    public void setParrier(Entity parrier) {
        if (parrier != null) {
            this.parrierUUID = parrier.getUuid();
            this.cachedParrier = parrier;
        }
    }
}
