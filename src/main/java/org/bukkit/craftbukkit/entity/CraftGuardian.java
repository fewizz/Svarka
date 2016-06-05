package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityGuardian;

public class CraftGuardian extends CraftMonster implements Guardian {

    public CraftGuardian(CraftServer server, EntityGuardian entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftGuardian";
    }

    @Override
    public EntityType getType() {
        return EntityType.GUARDIAN;
    }

    @Override
    public boolean isElder() {
        return ((EntityGuardian)entity).isElder();
    }

    @Override
    public void setElder( boolean shouldBeElder ) {
        EntityGuardian entityGuardian = (EntityGuardian) entity;

        if (!isElder() && shouldBeElder) {
            entityGuardian.setElder(true);
        } else if (isElder() && !shouldBeElder) {
            entityGuardian.setElder(false);

            // Since minecraft does not reset the elder Guardian to a guardian we have to do that
            entity.setSize(0.85F, 0.85F);

            // Since aW() calls its supers it will try to re register attributes which is invalid
            // check these on update
            entityGuardian.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
            entityGuardian.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
            entityGuardian.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0D);
            entityGuardian.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);

            // Update pathfinding (random stroll back to 80)
            entityGuardian.wander.setExecutionChance(80);

            // Tell minecraft that we need persistence since the guardian changed
            entityGuardian.applyEntityAttributes();
        }
    }
}
