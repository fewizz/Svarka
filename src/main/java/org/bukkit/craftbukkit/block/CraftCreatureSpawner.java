package org.bukkit.craftbukkit.block;

import org.bukkit.Material;

import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.EntityType;

import net.minecraft.tileentity.TileEntityMobSpawner;

public class CraftCreatureSpawner extends CraftBlockState implements CreatureSpawner {
    private final TileEntityMobSpawner spawner;

    public CraftCreatureSpawner(final Block block) {
        super(block);

        spawner = (TileEntityMobSpawner) ((CraftWorld) block.getWorld()).getTileEntityAt(getX(), getY(), getZ());
    }

    public CraftCreatureSpawner(final Material material, TileEntityMobSpawner te) {
        super(material);
        spawner = te;
    }

    public EntityType getSpawnedType() {
        return EntityType.fromName(spawner.getSpawnerBaseLogic().getEntityNameToSpawn());
    }

    public void setSpawnedType(EntityType entityType) {
        if (entityType == null || entityType.getName() == null) {
            throw new IllegalArgumentException("Can't spawn EntityType " + entityType + " from mobspawners!");
        }

        spawner.getSpawnerBaseLogic().setEntityName(entityType.getName());
    }

    @Deprecated
    public String getCreatureTypeId() {
        return spawner.getSpawnerBaseLogic().getEntityNameToSpawn();
    }

    @Deprecated
    public void setCreatureTypeId(String creatureName) {
        setCreatureTypeByName(creatureName);
    }

    public String getCreatureTypeName() {
        return spawner.getSpawnerBaseLogic().getEntityNameToSpawn();
    }

    public void setCreatureTypeByName(String creatureType) {
        // Verify input
        EntityType type = EntityType.fromName(creatureType);
        if (type == null) {
            return;
        }
        setSpawnedType(type);
    }

    public int getDelay() {
        return spawner.getSpawnerBaseLogic().spawnDelay;
    }

    public void setDelay(int delay) {
        spawner.getSpawnerBaseLogic().spawnDelay = delay;
    }

    @Override
    public TileEntityMobSpawner getTileEntity() {
        return spawner;
    }
}
