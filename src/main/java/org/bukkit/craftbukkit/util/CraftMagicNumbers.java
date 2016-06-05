package org.bukkit.craftbukkit.util;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Achievement;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.UnsafeValues;
import org.bukkit.craftbukkit.CraftStatistic;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

@SuppressWarnings("deprecation")
public final class CraftMagicNumbers implements UnsafeValues {
    public static final UnsafeValues INSTANCE = new CraftMagicNumbers();

    private CraftMagicNumbers() {}

    public static Block getBlock(org.bukkit.block.Block block) {
        return getBlock(block.getType());
    }

    @Deprecated
    // A bad method for bad magic.
    public static Block getBlock(int id) {
        return getBlock(Material.getMaterial(id));
    }

    @Deprecated
    // A bad method for bad magic.
    public static int getId(Block block) {
        return Block.getIdFromBlock(block);
    }

    public static Material getMaterial(Block block) {
        return Material.getMaterial(Block.getIdFromBlock(block));
    }

    public static Item getItem(Material material) {
        // TODO: Don't use ID
        Item item = Item.getItemById(material.getId());
        return item;
    }

    @Deprecated
    // A bad method for bad magic.
    public static Item getItem(int id) {
        return Item.getItemById(id);
    }

    @Deprecated
    // A bad method for bad magic.
    public static int getId(Item item) {
        return Item.getIdFromItem(item);
    }

    public static Material getMaterial(Item item) {
        // TODO: Don't use ID
        Material material = Material.getMaterial(Item.getIdFromItem(item));

        if (material == null) {
            return Material.AIR;
        }

        return material;
    }

    public static Block getBlock(Material material) {
        if (material == null) {
            return null;
        }
        // TODO: Don't use ID
        Block block = Block.getBlockById(material.getId());

        if (block == null) {
            return Blocks.AIR;
        }

        return block;
    }

    @Override
    public Material getMaterialFromInternalName(String name) {
        return getMaterial((Item) Item.REGISTRY.getObject(new ResourceLocation(name)));
    }

    @Override
    public List<String> tabCompleteInternalMaterialName(String token, List<String> completions) {
        ArrayList<String> results = Lists.newArrayList();
        for (ResourceLocation key : (Set<ResourceLocation>)Item.REGISTRY.getKeys()) {
            results.add(key.toString());
        }
        return StringUtil.copyPartialMatches(token, results, completions);
    }

    @Override
    public ItemStack modifyItemStack(ItemStack stack, String arguments) {
    	net.minecraft.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);

        try {
            nmsStack.setTagCompound((NBTTagCompound) JsonToNBT.getTagFromJson(arguments));
        } catch (NBTException ex) {
            Logger.getLogger(CraftMagicNumbers.class.getName()).log(Level.SEVERE, null, ex);
        }

        stack.setItemMeta(CraftItemStack.getItemMeta(nmsStack));

        return stack;
    }

    @Override
    public Statistic getStatisticFromInternalName(String name) {
        return CraftStatistic.getBukkitStatisticByName(name);
    }

    @Override
    public Achievement getAchievementFromInternalName(String name) {
        return CraftStatistic.getBukkitAchievementByName(name);
    }

    @Override
    public List<String> tabCompleteInternalStatisticOrAchievementName(String token, List<String> completions) {
        List<String> matches = new ArrayList<String>();
        Iterator iterator = StatList.ALL_STATS.iterator();
        while (iterator.hasNext()) {
            String statistic = ((StatBase) iterator.next()).statId;
            if (statistic.startsWith(token)) {
                matches.add(statistic);
            }
        }
        return matches;
    }
}
