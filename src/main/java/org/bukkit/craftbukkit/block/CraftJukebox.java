package org.bukkit.craftbukkit.block;

import net.minecraft.block.BlockJukebox;
import net.minecraft.block.BlockJukebox.TileEntityJukebox;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.*;
import net.minecraft.util.math.BlockPos;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;

public class CraftJukebox extends CraftBlockState implements Jukebox {
    private final CraftWorld world;
    private final TileEntityJukebox jukebox;

    public CraftJukebox(final Block block) {
        super(block);

        world = (CraftWorld) block.getWorld();
        jukebox = (TileEntityJukebox) world.getTileEntityAt(getX(), getY(), getZ());
    }

    public CraftJukebox(final Material material, TileEntityJukebox te) {
        super(material);
        world = null;
        jukebox = te;
    }

    @Override
    public Material getPlaying() {
        ItemStack record = jukebox.getRecord();
        if (record == null) {
            return Material.AIR;
        }
        return CraftMagicNumbers.getMaterial(record.getItem());
    }

    @Override
    public void setPlaying(Material record) {
        if (record == null || CraftMagicNumbers.getItem(record) == null) {
            record = Material.AIR;
            jukebox.setRecord(null);
        } else {
            jukebox.setRecord(new ItemStack(CraftMagicNumbers.getItem(record), 1));
        }
        if (!isPlaced()) {
            return;
        }
        jukebox.markDirty();
        
        if (record == Material.AIR) {
            world.getHandle().setTypeAndData(new BlockPos(getX(), getY(), getZ()),
                Blocks.JUKEBOX.getDefaultState().withProperty(BlockJukebox.HAS_RECORD, false), 3);
        } else {
            world.getHandle().setTypeAndData(new BlockPos(getX(), getY(), getZ()),
                Blocks.JUKEBOX.getDefaultState().withProperty(BlockJukebox.HAS_RECORD, true), 3);
        }
        world.playEffect(getLocation(), Effect.RECORD_PLAY, record.getId());
    }

    public boolean isPlaying() {
        return getRawData() == 1;
    }

    public boolean eject() {
        requirePlaced();
        boolean result = isPlaying();
        ((BlockJukebox) Blocks.JUKEBOX).dropRecord(world.getHandle(), new BlockPos(getX(), getY(), getZ()), null);
        return result;
    }

    @Override
    public TileEntityJukebox getTileEntity() {
        return jukebox;
    }
}
