package org.bukkit.craftbukkit.entity;

import java.util.Collection;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.google.common.collect.ImmutableList;

import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.potion.PotionUtils;

public abstract class CraftThrownPotion extends CraftProjectile implements ThrownPotion {
    public CraftThrownPotion(CraftServer server, EntityPotion entity) {
        super(server, entity);
    }

    public Collection<PotionEffect> getEffects() {
        ImmutableList.Builder<PotionEffect> builder = ImmutableList.builder();
        for (net.minecraft.potion.PotionEffect effect : PotionUtils.getEffectsFromStack(getHandle().getPotion())) {
            builder.add(CraftPotionUtil.toBukkit(effect));
        }
        return builder.build();
    }

    public ItemStack getItem() {
        return CraftItemStack.asBukkitCopy(getHandle().getPotion());
    }

    @Override
    public EntityPotion getHandle() {
        return (EntityPotion) entity;
    }
}
