package ru.svarka.tileentity;

import net.minecraft.tileentity.TileEntityLockable;
import ru.svarka.inventory.ICBLockableContainer;

public abstract class CBTileEntityLockable extends TileEntityLockable implements ICBLockableContainer {
    // CraftBukkit start
    @Override
    public org.bukkit.Location getLocation() {
    	return new org.bukkit.Location(worldObj.getWorld(), pos.getX(), pos.getY(), pos.getZ());
    }
    // CraftBukkit end
}
