// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import org.bukkit.inventory.Recipe;
import java.util.Iterator;

public class RecipeIterator implements Iterator<Recipe>
{
    private final Iterator<IRecipe> recipes;
    private final Iterator<ItemStack> smeltingCustom;
    private final Iterator<ItemStack> smeltingVanilla;
    private Iterator<?> removeFrom;
    
    public RecipeIterator() {
        this.removeFrom = null;
        this.recipes = CraftingManager.getInstance().getRecipeList().iterator();
        this.smeltingCustom = FurnaceRecipes.instance().customRecipes.keySet().iterator();
        this.smeltingVanilla = FurnaceRecipes.instance().smeltingList.keySet().iterator();
    }
    
    @Override
    public boolean hasNext() {
        return this.recipes.hasNext() || this.smeltingCustom.hasNext() || this.smeltingVanilla.hasNext();
    }
    
    @Override
    public Recipe next() {
        if (this.recipes.hasNext()) {
            this.removeFrom = this.recipes;
            return this.recipes.next().toBukkitRecipe();
        }
        ItemStack item;
        if (this.smeltingCustom.hasNext()) {
            this.removeFrom = this.smeltingCustom;
            item = this.smeltingCustom.next();
        }
        else {
            this.removeFrom = this.smeltingVanilla;
            item = this.smeltingVanilla.next();
        }
        final CraftItemStack stack = CraftItemStack.asCraftMirror(FurnaceRecipes.instance().getSmeltingResult(item));
        return new CraftFurnaceRecipe(stack, CraftItemStack.asCraftMirror(item));
    }
    
    @Override
    public void remove() {
        if (this.removeFrom == null) {
            throw new IllegalStateException();
        }
        this.removeFrom.remove();
    }
}
