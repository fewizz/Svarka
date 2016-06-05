package org.bukkit.craftbukkit.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.projectiles.CraftBlockProjectileSource;
import org.bukkit.inventory.Inventory;
import org.bukkit.projectiles.BlockProjectileSource;

import net.minecraft.block.BlockDispenser;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.math.BlockPos;

public class CraftDispenser extends CraftBlockState implements Dispenser {
    private final CraftWorld world;
    private final TileEntityDispenser dispenser;

    public CraftDispenser(final Block block) {
        super(block);

        world = (CraftWorld) block.getWorld();
        dispenser = (TileEntityDispenser) world.getTileEntityAt(getX(), getY(), getZ());
    }

    public CraftDispenser(final Material material, final TileEntityDispenser te) {
        super(material);
        world = null;
        dispenser = te;
    }

    public Inventory getInventory() {
        return new CraftInventory(dispenser);
    }

    public BlockProjectileSource getBlockProjectileSource() {
        Block block = getBlock();

        if (block.getType() != Material.DISPENSER) {
            return null;
        }

        return new CraftBlockProjectileSource(dispenser);
    }

    public boolean dispense() {
        Block block = getBlock();

        if (block.getType() == Material.DISPENSER) {
            BlockDispenser dispense = (BlockDispenser) Blocks.DISPENSER;

            dispense.dispense(world.getHandle(), new BlockPos(getX(), getY(), getZ()));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            dispenser.markDirty();
        }

        return result;
    }

    @Override
    public TileEntityDispenser getTileEntity() {
        return dispenser;
    }
}
