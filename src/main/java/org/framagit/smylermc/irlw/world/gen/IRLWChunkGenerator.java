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

import java.io.IOException;
import java.util.Random;

import org.framagit.smylermc.irlw.maps.TileFactory;
import org.framagit.smylermc.irlw.maps.TiledMap;
import org.framagit.smylermc.irlw.maps.exceptions.InvalidMapboxSessionException;
import org.framagit.smylermc.irlw.maps.tiles.RasterWebTile.InvalidTileCoordinatesException;
import org.framagit.smylermc.irlw.maps.tiles.tiles.MapboxElevationTile;
import org.framagit.smylermc.irlw.maps.utils.MapboxUtils;
import org.framagit.smylermc.irlw.maps.utils.WebMercatorUtils;
import org.framagit.smylermc.irlw.world.WorldConstants;

import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.jigsaw.JigsawJunction;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;

/**
 * @author SmylerMC
 *
 * TODO Type comment
 *
 */
//FIXME 1.14.4 - IRLW Chunk generator
public class IRLWChunkGenerator extends ChunkGenerator<IRLWGenerationSettings> {

	protected IRLWGenerationSettings genSettings;
	protected double centerLong;
	protected double centerLat;
	protected int zoomLevel;
	protected long xDelta;
	protected long zDelta;
	private TiledMap<MapboxElevationTile> heightMap;

	public IRLWChunkGenerator(IWorld worldIn, BiomeProvider biomeProviderIn, IRLWGenerationSettings generationSettings) {
		super(worldIn, biomeProviderIn, generationSettings);
		this.genSettings = generationSettings;
		this.centerLong = generationSettings.getSpawnLong();
		this.centerLat = generationSettings.getSpawnLat();
		this.zoomLevel = generationSettings.getZoomLevel();
		this.xDelta = (long) WebMercatorUtils.getXFromLongitude(this.centerLong, zoomLevel)/16;
		this.zDelta = (long) WebMercatorUtils.getYFromLatitude(this.centerLat, zoomLevel)/16;
		this.xDelta *= 16;	//We are just rounding up to match chunks
		this.zDelta *= 16;
		this.heightMap = new TiledMap<MapboxElevationTile>(TileFactory.MAPBOX_ELEVATION_TILE_FACTORY, this.zoomLevel);
		this.heightMap.enableSmartLoading();
		this.defaultFluid = generationSettings.getDefaultFluid();
		this.defaultBlock = generationSettings.getDefaultBlock();
	}

	//	private OverworldChunkGenerator overwoldGenerator;
	//	
	//	private final static int WATER_LEVEL = 63;


	//	
	//	//TODO Implement new generation system
	//	@Override
	//	public Chunk generateChunk(int x, int z) {
	//		
	//		Chunk chunk = new IRLWEmptyChunk(this.world, x, z);
	//		
	//		try {
	//			ChunkPrimer primer = new ChunkPrimer();
	//			this.setBlocksInChunk(x, z, primer);
	//			Biome[] biomesForGeneration = this.world.getBiomeProvider().getBiomes(new Biome[] {}, x * 16, z * 16, 16, 16);
	//		    this.overwoldGenerator.replaceBiomeBlocks(x, z, primer, biomesForGeneration);
	//		    chunk = new Chunk(world, primer, x, z);
	//		} catch (Exception e) {
	//			if(e instanceof RasterWebTile.InvalidTileCoordinatesException){
	//				chunk = new Chunk(world, x, z);
	//			} else {
	//				IRLW.logger.error("We got an exception when generating! See stack trace. You may report bugs to " + IRLW.AUTHOR_EMAIL);
	//				IRLW.logger.error("Here is more info: \n\tZoom level: " + this.zoomLevel +
	//						"\n\tDelta X: " + this.xDelta + " Delta Z: " + this.zDelta +
	//						"\n\tChunk: (" + x + ";" + z);
	//				IRLW.logger.catching(e);
	//				IRLW.proxy.onGenerationError(world, e);
	//			}
	//		}
	//		
	//	    chunk.generateSkylightMap();
	//	    return chunk;
	//	    
	//	}
	//	
	//	/*
	//	 * This is all temporary, but works
	//	 */
	//	public void setBlocksInChunk(int x, int z, ChunkPrimer primer) throws IOException, InvalidMapboxSessionException {
	//		for(int cx = 0; cx<16; cx++) {
	//			for(int cz = 0; cz<16; cz++) {
	//				int height = (int) (Math.round(getHeightFromData((long)x*16+cx + this.xDelta, (long)z*16+cz+ this.zDelta))/(WorldConstants.EVREST_ALTITUDE/192));
	////				int heightUp = (int) (Math.round(getHeightFromData((long)x*16+cx+1+ this.xDelta, (long)z*16+cz+ this.zDelta))/(IRLWChunkGenerator.EVREST_ALTITUDE/192));
	////				int heightDown = (int) (Math.round(getHeightFromData((long)x*16+cx-1 + this.xDelta, (long)z*16+cz + this.zDelta))/(IRLWChunkGenerator.EVREST_ALTITUDE/192));
	////				int heightLeft = (int) (Math.round(getHeightFromData((long)x*16+cx + this.xDelta, (long)z*16+cz+1+ this.zDelta))/(IRLWChunkGenerator.EVREST_ALTITUDE/192));
	////				int heightRight = (int) (Math.round(getHeightFromData((long)x*16+cx+ this.xDelta, (long)z*16+cz-1+ this.zDelta))/(IRLWChunkGenerator.EVREST_ALTITUDE/192));
	//				if(height!=0) height += 63; else height = 43;
	////				if(heightUp!=0) heightUp += 63;else heightUp = 43;
	////				if(heightDown!=0) heightDown += 63;else heightDown = 43;
	////				if(heightLeft!=0) heightLeft += 63;else heightLeft = 43;
	////				if(heightRight!=0) heightRight += 63;else heightRight = 43;
	////				height = (height +  heightUp + heightDown + heightLeft + heightRight) / 5 ; 
	//				int cy=0;
	//				for(; cy<height;cy++) {
	//					primer.setBlockState(cx, cy, cz, Blocks.STONE.getDefaultState());
	//				}
	//				for(; cy<IRLWChunkGenerator.WATER_LEVEL;cy++) {
	//					primer.setBlockState(cx, cy, cz, Blocks.WATER.getDefaultState());
	//				}
	//			}
	//		}
	//	}
	//	
	//	
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


	/* Getters and Setters from there */

	public int getZoomLevel() {
		return this.zoomLevel;
	}

	public long getDeltaX() {
		return this.xDelta;
	}

	public long getDeltaZ() {
		return this.zDelta;
	}

	public double getCenterLatitude() {
		return this.centerLat;
	}

	public double getCenterLongitude() {
		return this.centerLong;
	}

	private static final BlockState AIR = Blocks.AIR.getDefaultState();
	protected final BlockState defaultBlock;
	protected final BlockState defaultFluid;

	public void generateSurface(IChunk chunkIn) {
		ChunkPos chunkpos = chunkIn.getPos();
		int i = chunkpos.x;
		int j = chunkpos.z;
		SharedSeedRandom sharedseedrandom = new SharedSeedRandom();
		sharedseedrandom.setBaseChunkSeed(i, j);
		ChunkPos chunkpos1 = chunkIn.getPos();
		int k = chunkpos1.getXStart();
		int l = chunkpos1.getZStart();
		double d0 = 0.0625D;
		Biome[] abiome = chunkIn.getBiomes();

		for(int i1 = 0; i1 < 16; ++i1) {
			for(int j1 = 0; j1 < 16; ++j1) {
				int k1 = k + i1;
				int l1 = l + j1;
				int i2 = chunkIn.getTopBlockY(Heightmap.Type.WORLD_SURFACE_WG, i1, j1) + 1;
				double d1 = 64;
				abiome[j1 * 16 + i1].buildSurface(sharedseedrandom, chunkIn, k1, l1, i2, d1, this.getSettings().getDefaultBlock(), this.getSettings().getDefaultFluid(), this.getSeaLevel(), this.world.getSeed());
			}
		}

		this.makeBedrock(chunkIn, sharedseedrandom);
	}

	protected void makeBedrock(IChunk chunkIn, Random rand) {
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
		int i = chunkIn.getPos().getXStart();
		int j = chunkIn.getPos().getZStart();
		IRLWGenerationSettings t = this.getSettings();
		int k = t.getBedrockFloorHeight();
		int l = t.getBedrockRoofHeight();

		for(BlockPos blockpos : BlockPos.getAllInBoxMutable(i, 0, j, i + 15, 0, j + 15)) {
			if (l > 0) {
				for(int i1 = l; i1 >= l - 4; --i1) {
					if (i1 >= l - rand.nextInt(5)) {
						chunkIn.setBlockState(blockpos$mutableblockpos.setPos(blockpos.getX(), i1, blockpos.getZ()), Blocks.BEDROCK.getDefaultState(), false);
					}
				}
			}

			if (k < 256) {
				for(int j1 = k + 4; j1 >= k; --j1) {
					if (j1 <= k + rand.nextInt(5)) {
						chunkIn.setBlockState(blockpos$mutableblockpos.setPos(blockpos.getX(), j1, blockpos.getZ()), Blocks.BEDROCK.getDefaultState(), false);
					}
				}
			}
		}

	}

	@Override
	public int getGroundHeight() {
		return this.getSeaLevel() + 1;
	}

	@Override
	public void makeBase(IWorld worldIn, IChunk chunkIn) {

		
		int x  = chunkIn.getPos().x;
		int z  = chunkIn.getPos().z;
		ChunkPrimer primer = (ChunkPrimer)chunkIn;
		for(int cx = 0; cx<16; cx++) {
			for(int cz = 0; cz<16; cz++) {
				int height = 0;
				int heightUp = 0;
				int heightDown = 0;
				int heightLeft = 0;
				int heightRight = 0;
				try {
					height = (int) (Math.round(getHeightFromData((long)x*16+cx + this.xDelta, (long)z*16+cz+ this.zDelta))/(WorldConstants.EVREST_ALTITUDE/192));
					heightUp = (int) (Math.round(getHeightFromData((long)x*16+cx+1+ this.xDelta, (long)z*16+cz+ this.zDelta))/(WorldConstants.EVREST_ALTITUDE/192));
					heightDown = (int) (Math.round(getHeightFromData((long)x*16+cx-1 + this.xDelta, (long)z*16+cz + this.zDelta))/(WorldConstants.EVREST_ALTITUDE/192));
					heightLeft = (int) (Math.round(getHeightFromData((long)x*16+cx + this.xDelta, (long)z*16+cz+1+ this.zDelta))/(WorldConstants.EVREST_ALTITUDE/192));
					heightRight = (int) (Math.round(getHeightFromData((long)x*16+cx+ this.xDelta, (long)z*16+cz-1+ this.zDelta))/(WorldConstants.EVREST_ALTITUDE/192));
				} catch (IOException | InvalidMapboxSessionException | InvalidTileCoordinatesException e) {
					// TODO Auto-generated catch block
					//								e.printStackTrace();
				}

				if(height!=0) height += 63; else height = 43;
				if(heightUp!=0) heightUp += 63;else heightUp = 43;
				if(heightDown!=0) heightDown += 63;else heightDown = 43;
				if(heightLeft!=0) heightLeft += 63;else heightLeft = 43;
				if(heightRight!=0) heightRight += 63;else heightRight = 43;
				height = (height +  heightUp + heightDown + heightLeft + heightRight) / 5 ;

				int cy=0;
				for(; cy<height;cy++) {
					primer.setBlockState(new BlockPos(cx, cy, cz), Blocks.STONE.getDefaultState(), false);
				}
				for(; cy<this.getSeaLevel();cy++) {
					primer.setBlockState(new BlockPos(cx, cy, cz), Blocks.WATER.getDefaultState(), false);
				}
			}
		}
		primer.setLight(true);
	}

	@Override
	public int func_222529_a(int p_222529_1_, int p_222529_2_, Type p_222529_3_) {
		// TODO Auto-generated method stub
		return 0;
	}

}
