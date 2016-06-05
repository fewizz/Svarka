package org.bukkit.craftbukkit.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.server.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkGenerator;

import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.generator.BlockPopulator;

public class NormalChunkGenerator extends InternalChunkGenerator {
    private final IChunkGenerator generator;

    public NormalChunkGenerator(World world, long seed) {
        generator = world.provider.createChunkGenerator();
    }

    @Override
    public byte[] generate(org.bukkit.World world, Random random, int x, int z) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean canSpawn(org.bukkit.World world, int x, int z) {
        return ((CraftWorld) world).getHandle().provider.canCoordinateBeSpawn(x, z);
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(org.bukkit.World world) {
        return new ArrayList<BlockPopulator>();
    }

    @Override
    public Chunk provideChunk(int i, int i1) {
        return generator.provideChunk(i, i1);
    }

    @Override
    public void populate(int i, int i1) {
        generator.populate(i, i1);
    }

    @Override
    public boolean generateStructures(Chunk chunk, int i, int i1) {
        return generator.generateStructures(chunk, i, i1);
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType enumCreatureType, BlockPos blockPosition) {
        return generator.getPossibleCreatures(enumCreatureType, blockPosition);
    }

    @Override
    public BlockPos getStrongholdGen(World world, String s, BlockPos blockPosition) {
        return generator.getStrongholdGen(world, s, blockPosition);
    }

    @Override
    public void recreateStructures(Chunk chunk, int i, int i1) {
        generator.recreateStructures(chunk, i, i1);
    }
}
