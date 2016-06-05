package org.bukkit.craftbukkit.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.craftbukkit.CraftWorld;

import net.minecraft.tileentity.TileEntityCommandBlock;

public class CraftCommandBlock extends CraftBlockState implements CommandBlock {
    private final TileEntityCommandBlock commandBlock;
    private String command;
    private String name;

    public CraftCommandBlock(Block block) {
        super(block);

        CraftWorld world = (CraftWorld) block.getWorld();
        commandBlock = (TileEntityCommandBlock) world.getTileEntityAt(getX(), getY(), getZ());
        command = commandBlock.getCommandBlockLogic().getCommand();
        name = commandBlock.getCommandBlockLogic().getName();
    }

    public CraftCommandBlock(final Material material, final TileEntityCommandBlock te) {
        super(material);
        commandBlock = te;
        command = commandBlock.getCommandBlockLogic().getCommand();
        name = commandBlock.getCommandBlockLogic().getName();
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command != null ? command : "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name : "@";
    }

    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            commandBlock.getCommandBlockLogic().setCommand(command);
            commandBlock.getCommandBlockLogic().setName(name);
        }

        return result;
    }

    @Override
    public TileEntityCommandBlock getTileEntity() {
        return commandBlock;
    }
}
