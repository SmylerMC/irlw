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
package com.github.smylermc.irlw.world.gen;

import java.io.IOException;
import java.util.List;

import com.github.smylermc.irlw.IRLW;
import com.github.smylermc.irlw.maps.TileFactory;
import com.github.smylermc.irlw.maps.TiledMap;
import com.github.smylermc.irlw.maps.exceptions.InvalidMapboxSessionException;
import com.github.smylermc.irlw.maps.tiles.RasterWebTile;
import com.github.smylermc.irlw.maps.tiles.tiles.MapboxElevationTile;
import com.github.smylermc.irlw.maps.utils.MapboxUtils;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraft.world.gen.IChunkGenerator;

/**
 * @author SmylerMC
 *
 * TODO Type comment
 *
 */
public class IRLWChunkGenerator implements IChunkGenerator{

	private World world;
	private long seed;
	
	//TODO implement
	private double centerLong = 0;
	private double centerLat = 0;
	private int zoomLevel = 0;
	private TiledMap<MapboxElevationTile> heightMap;
	
	private ChunkGeneratorOverworld overwoldGenerator;
	
	private final static int EVREST_ALTITUDE = 8848;
	private final static int WATER_LEVEL = 63;
	
	/**
	 * @param worldIn
	 * @param seed
	 * @param mapFeaturesEnabledIn
	 * @param generatorOptions
	 */
	public IRLWChunkGenerator(World worldIn, long seed, double centerLong, double centerLat, int zoomLevel) {
		this.centerLong = centerLong;
		this.centerLat = centerLat;
		this.zoomLevel = zoomLevel;
		this.world = worldIn;
		this.seed = seed;
		this.heightMap = new TiledMap<MapboxElevationTile>(TileFactory.MAPBOX_ELEVATION_TILE_FACTORY, this.zoomLevel);
		this.overwoldGenerator = new ChunkGeneratorOverworld(worldIn, seed, true, "");
	}
	
	@Override
	public Chunk generateChunk(int x, int z) {
		
		try {
			ChunkPrimer primer = new ChunkPrimer();
			this.setBlocksInChunk(x, z, primer);
			Biome[] biomesForGeneration = this.world.getBiomeProvider().getBiomes(new Biome[] {}, x * 16, z * 16, 16, 16);
		    this.overwoldGenerator.replaceBiomeBlocks(x, z, primer, biomesForGeneration);
		    Chunk chunk = new Chunk(world, primer, x, z);
		    chunk.generateSkylightMap();
		    return chunk;
		} catch (Exception e) {
			if(!(e instanceof RasterWebTile.InvalidTileCoordinatesException)){
				IRLW.logger.catching(e);
				IRLW.proxy.onGenerationError(world, e);
			}
			return new EmptyChunk(this.world, x, z);
		}
	}
	
	public void setBlocksInChunk(int x, int z, ChunkPrimer primer) throws IOException, InvalidMapboxSessionException {
		for(int cx = 0; cx<16; cx++) {
			for(int cz = 0; cz<16; cz++) {
				int height = (int) (Math.round(getHeightFromData((long)x*16+cx, (long)z*16+cz))/(IRLWChunkGenerator.EVREST_ALTITUDE/192));
				int heightUp = (int) (Math.round(getHeightFromData((long)x*16+cx+1, (long)z*16+cz))/(IRLWChunkGenerator.EVREST_ALTITUDE/192));
				int heightDown = (int) (Math.round(getHeightFromData((long)x*16+cx-1, (long)z*16+cz))/(IRLWChunkGenerator.EVREST_ALTITUDE/192));
				int heightLeft = (int) (Math.round(getHeightFromData((long)x*16+cx, (long)z*16+cz+1))/(IRLWChunkGenerator.EVREST_ALTITUDE/192));
				int heightRight = (int) (Math.round(getHeightFromData((long)x*16+cx, (long)z*16+cz-1))/(IRLWChunkGenerator.EVREST_ALTITUDE/192));
				if(height!=0) height += 63; else height = 43;
				if(heightUp!=0) heightUp += 63;else heightUp = 43;
				if(heightDown!=0) heightDown += 63;else heightDown = 43;
				if(heightLeft!=0) heightLeft += 63;else heightLeft = 43;
				if(heightRight!=0) heightRight += 63;else heightRight = 43;
				height = (height +  heightUp + heightDown + heightLeft + heightRight) / 5 ;
				int cy=0;
				for(; cy<height;cy++) {
					primer.setBlockState(cx, cy, cz, Blocks.STONE.getDefaultState());
				}
				for(; cy<IRLWChunkGenerator.WATER_LEVEL;cy++) {
					primer.setBlockState(cx, cy, cz, Blocks.WATER.getDefaultState());
				}
			}
		}
	}
	
	
	/**
	 * Gets the terrain height at x and y on the tiled map of the corresponding zoom
	 * 
	 * @return the altitude in meter
	 * @throws InvalidMapboxSessionException 
	 * @throws IOException 
	 */
	private double getHeightFromData(long x, long y) throws IOException, InvalidMapboxSessionException {
		int[] pixel = this.heightMap.getPixel(x, y);
		return MapboxUtils.RGBAToHeight(pixel);
	}


	@Override
	public void populate(int x, int z) {
		this.overwoldGenerator.populate(x, z);		
	}


	@Override
	public boolean generateStructures(Chunk chunkIn, int x, int z) {
		return this.overwoldGenerator.generateStructures(chunkIn, x, z);
//		return false; //TODO It gets crazy generating chests otherwise
	}


	@Override
	public List<SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
		return this.overwoldGenerator.getPossibleCreatures(creatureType, pos);
	}


	@Override
	public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position,
			boolean findUnexplored) {
		return this.overwoldGenerator.getNearestStructurePos(worldIn, structureName, position, findUnexplored);
	}


	@Override
	public void recreateStructures(Chunk chunkIn, int x, int z) {
		this.overwoldGenerator.recreateStructures(chunkIn, x, z);
		//TODO It gets crazy generating chests otherwise
	}


	@Override
	public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
		return this.overwoldGenerator.isInsideStructure(worldIn, structureName, pos);
	}

}
