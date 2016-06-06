package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;

import net.minecraft.entity.monster.EntityZombie;

public class CraftZombie extends CraftMonster implements Zombie {

    public CraftZombie(CraftServer server, EntityZombie entity) {
        super(server, entity);
    }

    @Override
    public EntityZombie getHandle() {
        return (EntityZombie) entity;
    }

    @Override
    public String toString() {
        return "CraftZombie";
    }

    public EntityType getType() {
        return EntityType.ZOMBIE;
    }

    public boolean isBaby() {
        return getHandle().isChild();
    }

    public void setBaby(boolean flag) {
        getHandle().setChild(flag);
    }

    public boolean isVillager() {
        return getHandle().isVillager();
    }

    public void setVillager(boolean flag) {
        if (flag) {
            getHandle().setVillagerType(0);
        } else {
            getHandle().setToNotVillager();
        }
    }

    @Override
    public void setVillagerProfession(Villager.Profession profession) {
        if (profession == null) {
            getHandle().setToNotVillager();
        } else {
            getHandle().setVillagerType(profession.getId());
        }
    }

    @Override
    public Villager.Profession getVillagerProfession() {
        if (!isVillager()) return null;
        return Villager.Profession.getProfession(getHandle().getVillagerType());
    }
}
