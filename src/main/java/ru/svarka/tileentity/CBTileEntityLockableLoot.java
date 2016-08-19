package ru.svarka.tileentity;

import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.tileentity.TileEntityLockableLoot;
import ru.svarka.inventory.ICBInventory;

public abstract class CBTileEntityLockableLoot extends TileEntityLockableLoot implements ICBInventory {
	// CraftBukkit start
    @Override
    public org.bukkit.Location getLocation() {
    	return new org.bukkit.Location(worldObj.getWorld(), pos.getX(), pos.getY(), pos.getZ());
    }
    // CraftBukkit end
}
