package org.bukkit.craftbukkit.inventory;

import org.bukkit.inventory.BeaconInventory;
import org.bukkit.inventory.ItemStack;

import net.minecraft.tileentity.TileEntityBeacon;

public class CraftInventoryBeacon extends CraftInventory implements BeaconInventory {
    public CraftInventoryBeacon(TileEntityBeacon beacon) {
        super(beacon);
    }

    public void setItem(ItemStack item) {
        setItem(0, item);
    }

    public ItemStack getItem() {
        return getItem(0);
    }
}
