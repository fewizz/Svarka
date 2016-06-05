package org.bukkit.craftbukkit.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.FlowerPot;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.material.MaterialData;

import net.minecraft.tileentity.TileEntityFlowerPot;

public class CraftFlowerPot extends CraftBlockState implements FlowerPot {

    private final TileEntityFlowerPot pot;

    public CraftFlowerPot(Block block) {
        super(block);

        pot = (TileEntityFlowerPot) ((CraftWorld) block.getWorld()).getTileEntityAt(getX(), getY(), getZ());
    }

    public CraftFlowerPot(Material material, TileEntityFlowerPot pot) {
        super(material);

        this.pot = pot;
    }

    @Override
    public MaterialData getContents() {
        return (pot.getFlowerPotItem() == null) ? null : CraftMagicNumbers.getMaterial(pot.getFlowerPotItem()).getNewData((byte) pot.getFlowerPotData()); // PAIL: rename
    }

    @Override
    public void setContents(MaterialData item) {
        if (item == null) {
            pot.setFlowerPotData(null, 0);
        } else {
            pot.setFlowerPotData(CraftMagicNumbers.getItem(item.getItemType()), item.getData()); // PAIL: rename
        }
    }
}
