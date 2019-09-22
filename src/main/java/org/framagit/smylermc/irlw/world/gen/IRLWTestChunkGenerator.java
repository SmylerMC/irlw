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

import net.minecraft.world.IWorld;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap.Type;

/**
 * @author SmylerMC
 *
 * TODO Type comment
 *
 */
//FIXME 1.14.4 - IRLW Test Chunk generator
public class IRLWTestChunkGenerator extends ChunkGenerator{

	public IRLWTestChunkGenerator(IWorld worldIn, BiomeProvider biomeProviderIn,
			GenerationSettings generationSettingsIn) {
		super(worldIn, biomeProviderIn, generationSettingsIn);
		// TODO Auto-generated constructor stub
	}
//
//	private World world;
//	
//	/**
//	 * @param worldIn
//	 * @param seed
//	 * @param mapFeaturesEnabledIn
//	 * @param generatorOptions
//	 */
//	public IRLWTestChunkGenerator(World worldIn) {
//		this.world = worldIn;
//	}
//	
//	@Override
//	public Chunk generateChunk(int x, int z) {
//		
//		if(x == 0) {
//			return new EmptyChunk(world ,z, x);
//		}else if(z == 0) {
//			return new IRLWEmptyChunk(world, z, x);
//		}
//		ChunkPrimer primer = new ChunkPrimer();
//		this.setBlocksInChunk(x, z, primer);
//		Chunk chunk = new Chunk(this.world, primer, x, z);
//		
//	    chunk.generateSkylightMap();
//	    return chunk;
//	    
//	}
//	
//	/*
//	 * This is all temporary, but works
//	 */
//	public void setBlocksInChunk(int x, int z, ChunkPrimer primer){
//		for(int cx = 0; cx<16; cx++) {
//			for(int cz = 0; cz<16; cz++) {
//				int height = 66;
//				int cy=0;
//				for(; cy<height;cy++) {
//					primer.setBlockState(cx, cy, cz, Blocks.STONE.getDefaultState());
//				}
//			}
//		}
//	}
//
//	@Override
//	public void populate(int x, int z) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public boolean generateStructures(Chunk chunkIn, int x, int z) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public List<SpawnListEntry> getPossibleCreatures(EntityClassification creatureType, BlockPos pos) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position,
//			boolean findUnexplored) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void recreateStructures(Chunk chunkIn, int x, int z) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
	@Override
	public void generateSurface(IChunk chunkIn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getGroundHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void makeBase(IWorld worldIn, IChunk chunkIn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int func_222529_a(int p_222529_1_, int p_222529_2_, Type p_222529_3_) {
		// TODO Auto-generated method stub
		return 0;
	}

}
