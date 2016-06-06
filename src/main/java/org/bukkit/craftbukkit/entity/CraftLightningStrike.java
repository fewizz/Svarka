package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LightningStrike;

import net.minecraft.entity.effect.EntityLightningBolt;

public class CraftLightningStrike extends CraftEntity implements LightningStrike {
    public CraftLightningStrike(final CraftServer server, final EntityLightningBolt entity) {
        super(server, entity);
    }

    public boolean isEffect() {
        return ((EntityLightningBolt) super.getHandle()).isEffect;
    }

    @Override
    public EntityLightningBolt getHandle() {
        return (EntityLightningBolt) entity;
    }

    @Override
    public String toString() {
        return "CraftLightningStrike";
    }

    public EntityType getType() {
        return EntityType.LIGHTNING;
    }
}
