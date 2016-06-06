package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PoweredMinecart;

import net.minecraft.entity.item.EntityMinecartFurnace;

@SuppressWarnings("deprecation")
public class CraftMinecartFurnace extends CraftMinecart implements PoweredMinecart {
    public CraftMinecartFurnace(CraftServer server, EntityMinecartFurnace entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftMinecartFurnace";
    }

    public EntityType getType() {
        return EntityType.MINECART_FURNACE;
    }
}
