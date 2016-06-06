package org.bukkit.craftbukkit.entity;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.village.MerchantRecipeList;

import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftMerchantRecipe;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.MerchantRecipe;

public class CraftVillager extends CraftAgeable implements Villager, InventoryHolder {

    public CraftVillager(CraftServer server, EntityVillager entity) {
        super(server, entity);
    }

    @Override
    public EntityVillager getHandle() {
        return (EntityVillager) entity;
    }

    @Override
    public String toString() {
        return "CraftVillager";
    }

    public EntityType getType() {
        return EntityType.VILLAGER;
    }

    public Profession getProfession() {
        return Profession.getProfession(getHandle().getProfession());
    }

    public void setProfession(Profession profession) {
        Validate.notNull(profession);
        getHandle().setProfession(profession.getId());
    }

    @Override
    public Inventory getInventory() {
        return new CraftInventory(getHandle().villagerInventory);
    }

    @Override
    public List<MerchantRecipe> getRecipes() {
        return Collections.unmodifiableList(Lists.transform(getHandle().getRecipes(null), new Function<net.minecraft.village.MerchantRecipe, MerchantRecipe>() {
            @Override
            public MerchantRecipe apply(net.minecraft.village.MerchantRecipe recipe) {
                return recipe.asBukkit();
            }
        }));
    }

    @Override
    public void setRecipes(List<MerchantRecipe> list) {
        MerchantRecipeList recipes = getHandle().getRecipes(null);
        recipes.clear();
        for (MerchantRecipe m : list) {
            recipes.add(CraftMerchantRecipe.fromBukkit(m).toMinecraft());
        }
    }

    @Override
    public MerchantRecipe getRecipe(int i) {
        return getHandle().getRecipes(null).get(i).asBukkit();
    }

    @Override
    public void setRecipe(int i, MerchantRecipe merchantRecipe) {
        getHandle().getRecipes(null).set(i, CraftMerchantRecipe.fromBukkit(merchantRecipe).toMinecraft());
    }

    @Override
    public int getRecipeCount() {
        return getHandle().getRecipes(null).size();
    }

    @Override
    public boolean isTrading() {
        return getTrader() != null;
    }

    @Override
    public HumanEntity getTrader() {
        EntityPlayer eh = getHandle().getCustomer();
        return eh == null ? null : eh.getBukkitEntity();
    }

    @Override
    public int getRiches() {
        return getHandle().wealth;
    }

    @Override
    public void setRiches(int riches) {
        getHandle().wealth = riches;
    }
}
