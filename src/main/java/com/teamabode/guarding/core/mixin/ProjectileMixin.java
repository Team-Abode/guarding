package com.teamabode.guarding.core.mixin;

import com.teamabode.guarding.core.access.ProjectileAccessor;
import net.minecraft.entity.LazyEntityReference;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;

@Mixin(ProjectileEntity.class)
public class ProjectileMixin implements ProjectileAccessor {
    @Unique
    private final ProjectileEntity $this = ProjectileEntity.class.cast(this);

    @Nullable
    private LazyEntityReference<Entity> parrier;

    @Inject(method = "readCustomData", at = @At("HEAD"))
    private void guarding$readCustomData(ReadView view, CallbackInfo ci) {
        this.parrier = LazyEntityReference.fromData(view, "parrier");
    }

    @Inject(method = "writeCustomData", at = @At("HEAD"))
    private void guarding$writeCustomData(WriteView view, CallbackInfo ci) {
        LazyEntityReference.writeData(this.parrier, view, "parrier");
    }

    @Override
    public Entity getParrier() {
        return LazyEntityReference.resolve(this.parrier, $this.getWorld(), Entity.class);
    }

    @Override
    public void setParrier(@Nullable Entity parrier) {
        this.parrier = this.parrier != null ? new LazyEntityReference<>(parrier) : null;
    }
}
