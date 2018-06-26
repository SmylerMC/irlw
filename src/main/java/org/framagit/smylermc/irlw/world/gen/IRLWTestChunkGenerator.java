/* IRL World Minecraft Mod
    Copyright (C) 2017  Smyler

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
	along with this program. If not, see <http://www.gnu.org/licenses/>.

	The author can be contacted at smyler@mail.com

	File created on 2 mai 2018 
*/
package org.framagit.smylermc.irlw.world.gen;

import java.util.List;

import org.framagit.smylermc.irlw.world.IRLWEmptyChunk;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.gen.IChunkGenerator;

/**
 * @author SmylerMC
 *
 * TODO Type comment
 *
 */
public class IRLWTestChunkGenerator implements IChunkGenerator{

	private World world;
	
	/**
	 * @param worldIn
	 * @param seed
	 * @param mapFeaturesEnabledIn
	 * @param generatorOptions
	 */
	public IRLWTestChunkGenerator(World worldIn) {
		this.world = worldIn;
	}
	
	@Override
	public Chunk generateChunk(int x, int z) {
		
		if(x == 0) {
			return new EmptyChunk(world ,z, x);
		}else if(z == 0) {
			return new IRLWEmptyChunk(world, z, x);
		}
		ChunkPrimer primer = new ChunkPrimer();
		this.setBlocksInChunk(x, z, primer);
		Chunk chunk = new Chunk(this.world, primer, x, z);
		
	    chunk.generateSkylightMap();
	    return chunk;
	    
	}
	
	/*
	 * This is all temporary, but works
	 */
	public void setBlocksInChunk(int x, int z, ChunkPrimer primer){
		for(int cx = 0; cx<16; cx++) {
			for(int cz = 0; cz<16; cz++) {
				int height = 66;
				int cy=0;
				for(; cy<height;cy++) {
					primer.setBlockState(cx, cy, cz, Blocks.STONE.getDefaultState());
				}
			}
		}
	}

	@Override
	public void populate(int x, int z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean generateStructures(Chunk chunkIn, int x, int z) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position,
			boolean findUnexplored) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void recreateStructures(Chunk chunkIn, int x, int z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
		// TODO Auto-generated method stub
		return false;
	}

}
