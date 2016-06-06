package org.bukkit.craftbukkit.inventory;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;

import fewizz.svarka.inventory.IBukkitInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import org.bukkit.Location;

public class CraftInventoryCustom extends CraftInventory {
    public CraftInventoryCustom(InventoryHolder owner, InventoryType type) {
        super(new MinecraftInventory(owner, type));
    }

    public CraftInventoryCustom(InventoryHolder owner, InventoryType type, String title) {
        super(new MinecraftInventory(owner, type, title));
    }

    public CraftInventoryCustom(InventoryHolder owner, int size) {
        super(new MinecraftInventory(owner, size));
    }

    public CraftInventoryCustom(InventoryHolder owner, int size, String title) {
        super(new MinecraftInventory(owner, size, title));
    }

    static class MinecraftInventory implements IBukkitInventory {
        private final ItemStack[] items;
        private int maxStack = MAX_STACK;
        private final List<HumanEntity> viewers;
        private final String title;
        private InventoryType type;
        private final InventoryHolder owner;

        public MinecraftInventory(InventoryHolder owner, InventoryType type) {
            this(owner, type.getDefaultSize(), type.getDefaultTitle());
            this.type = type;
        }

        public MinecraftInventory(InventoryHolder owner, InventoryType type, String title) {
            this(owner, type.getDefaultSize(), title);
            this.type = type;
        }

        public MinecraftInventory(InventoryHolder owner, int size) {
            this(owner, size, "Chest");
        }

        public MinecraftInventory(InventoryHolder owner, int size, String title) {
            Validate.notNull(title, "Title cannot be null");
            this.items = new ItemStack[size];
            this.title = title;
            this.viewers = new ArrayList<HumanEntity>();
            this.owner = owner;
            this.type = InventoryType.CHEST;
        }

        @Override
        public int getSizeInventory() {
            return items.length;
        }

        @Override
        public ItemStack getStackInSlot(int i) {
            return items[i];
        }

        @Override
        public ItemStack decrStackSize(int i, int j) {
            ItemStack stack = this.getStackInSlot(i);
            ItemStack result;
            if (stack == null) return null;
            if (stack.stackSize <= j) {
                this.setInventorySlotContents(i, null);
                result = stack;
            } else {
                result = CraftItemStack.copyNMSStack(stack, j);
                stack.stackSize -= j;
            }
            this.markDirty();
            return result;
        }

        @Override
        public ItemStack removeStackFromSlot(int i) {
            ItemStack stack = this.getStackInSlot(i);
            ItemStack result;
            if (stack == null) return null;
            if (stack.stackSize <= 1) {
                this.setInventorySlotContents(i, null);
                result = stack;
            } else {
                result = CraftItemStack.copyNMSStack(stack, 1);
                stack.stackSize -= 1;
            }
            return result;
        }

        @Override
        public void setInventorySlotContents(int i, ItemStack itemstack) {
            items[i] = itemstack;
            if (itemstack != null && this.getInventoryStackLimit() > 0 && itemstack.stackSize > this.getInventoryStackLimit()) {
                itemstack.stackSize = this.getInventoryStackLimit();
            }
        }

        @Override
        public int getInventoryStackLimit() {
            return maxStack;
        }

        @Override
        public void setMaxStackSize(int size) {
            maxStack = size;
        }

        @Override
        public void markDirty() {}

        @Override
        public boolean isUseableByPlayer(EntityPlayer entityhuman) {
            return true;
        }

        @Override
        public ItemStack[] getContents() {
            return items;
        }

        @Override
        public void onOpen(CraftHumanEntity who) {
            viewers.add(who);
        }

        @Override
        public void onClose(CraftHumanEntity who) {
            viewers.remove(who);
        }

        @Override
        public List<HumanEntity> getViewers() {
            return viewers;
        }

        public InventoryType getType() {
            return type;
        }
        
        @Override
        public InventoryHolder getOwner() {
            return owner;
        }

        @Override
        public boolean isItemValidForSlot(int i, ItemStack itemstack) {
            return true;
        }

        @Override
        public void openInventory(EntityPlayer entityHuman) {
        }

        @Override
        public void closeInventory(EntityPlayer entityHuman) {
        }

        @Override
        public int getField(int i) {
            return 0;
        }

        @Override
        public void setField(int i, int j) {
        }

        @Override
        public int getFieldCount() {
            return 0;
        }

        @Override
        public void clear() {
        }

        @Override
        public String getName() {
            return title;
        }

        @Override
        public boolean hasCustomName() {
            return title != null;
        }

        @Override
        public ITextComponent getDisplayName() {
            return new TextComponentString(title);
        }

        @Override
        public Location getLocation() {
            return null;
        }
    }
}
