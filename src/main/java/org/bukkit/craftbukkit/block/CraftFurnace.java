package org.bukkit.craftbukkit.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventoryFurnace;
import org.bukkit.inventory.FurnaceInventory;

import net.minecraft.tileentity.TileEntityFurnace;

public class CraftFurnace extends CraftBlockState implements Furnace {
    private final TileEntityFurnace furnace;

    public CraftFurnace(final Block block) {
        super(block);

        furnace = (TileEntityFurnace) ((CraftWorld) block.getWorld()).getTileEntityAt(getX(), getY(), getZ());
    }

    public CraftFurnace(final Material material, final TileEntityFurnace te) {
        super(material);
        furnace = te;
    }

    public FurnaceInventory getInventory() {
        return new CraftInventoryFurnace(furnace);
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            furnace.update();
        }

        return result;
    }

    public short getBurnTime() {
        return (short) furnace.getField(0);
    }

    public void setBurnTime(short burnTime) {
        furnace.setField(0, burnTime);
    }

    public short getCookTime() {
        return (short) furnace.getField(2);
    }

    public void setCookTime(short cookTime) {
        furnace.setField(2, cookTime);
    }

    @Override
    public TileEntityFurnace getTileEntity() {
        return furnace;
    }
}
