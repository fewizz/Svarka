package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.minecart.RideableMinecart;

import net.minecraft.entity.item.EntityMinecart;

public class CraftMinecartRideable extends CraftMinecart implements RideableMinecart {
    public CraftMinecartRideable(CraftServer server, EntityMinecart entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftMinecartRideable";
    }

    public EntityType getType() {
        return EntityType.MINECART;
    }
}
