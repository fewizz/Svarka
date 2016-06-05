package org.bukkit.craftbukkit.entity;

import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingSand;

import net.minecraft.entity.item.EntityFallingBlock;

public class CraftFallingSand extends CraftEntity implements FallingSand {

    public CraftFallingSand(CraftServer server, EntityFallingBlock entity) {
        super(server, entity);
    }

    @Override
    public EntityFallingBlock getHandle() {
        return (EntityFallingBlock) entity;
    }

    @Override
    public String toString() {
        return "CraftFallingSand";
    }

    public EntityType getType() {
        return EntityType.FALLING_BLOCK;
    }

    public Material getMaterial() {
        return Material.getMaterial(getBlockId());
    }

    public int getBlockId() {
        return CraftMagicNumbers.getId(getHandle().getBlock().getBlock());
    }

    public byte getBlockData() {
        return (byte) getHandle().getBlock().getBlock().getMetaFromState(getHandle().getBlock());
    }

    public boolean getDropItem() {
        return getHandle().shouldDropItem;
    }

    public void setDropItem(boolean drop) {
        getHandle().shouldDropItem = drop;
    }

    @Override
    public boolean canHurtEntities() {
        return getHandle().hurtEntities;
    }

    @Override
    public void setHurtEntities(boolean hurtEntities) {
        getHandle().hurtEntities = hurtEntities;
    }
}
