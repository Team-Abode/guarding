package com.teamabode.guarding.core.access;

import net.minecraft.entity.Entity;

public interface ProjectileAccessor {
    Entity getParrier();

    void setParrier(Entity parrier);
}
