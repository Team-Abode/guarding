package com.teamabode.guarding.core.access;

import net.minecraft.world.entity.Entity;

public interface ProjectileAccessor {

    Entity getParrier();

    void setParrier(Entity parrier);
}
