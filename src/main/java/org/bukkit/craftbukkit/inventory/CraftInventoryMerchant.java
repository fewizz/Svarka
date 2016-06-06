package org.bukkit.craftbukkit.inventory;

import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;

import net.minecraft.inventory.InventoryMerchant;

public class CraftInventoryMerchant extends CraftInventory implements MerchantInventory {

    public CraftInventoryMerchant(InventoryMerchant merchant) {
        super(merchant);
    }

    @Override
    public int getSelectedRecipeIndex() {
        return getInventory().currentRecipeIndex;
    }

    @Override
    public MerchantRecipe getSelectedRecipe() {
        return getInventory().getCurrentRecipe().asBukkit();
    }

    @Override
    public InventoryMerchant getInventory() {
        return (InventoryMerchant) inventory;
    }
}
