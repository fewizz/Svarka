package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.WaterMob;

import net.minecraft.entity.passive.EntityWaterMob;

public class CraftWaterMob extends CraftLivingEntity implements WaterMob {

    public CraftWaterMob(CraftServer server, EntityWaterMob entity) {
        super(server, entity);
    }

    @Override
    public EntityWaterMob getHandle() {
        return (EntityWaterMob) entity;
    }

    @Override
    public String toString() {
        return "CraftWaterMob";
    }
}
