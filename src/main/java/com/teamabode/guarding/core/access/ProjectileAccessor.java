package com.teamabode.guarding.core.access;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LazyEntityReference;

public interface ProjectileAccessor {
    Entity getParrier();

    void setParrier(Entity parrier);

    void setParrier(LazyEntityReference<Entity> parrier);
}
