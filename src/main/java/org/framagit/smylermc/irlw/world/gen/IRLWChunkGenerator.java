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

import org.framagit.smylermc.irlw.maps.TiledMap;
import org.framagit.smylermc.irlw.maps.tiles.tiles.MapboxElevationTile;

import net.minecraft.world.IWorld;
import net.minecraft.world.World;
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
//FIXME 1.14.4 - IRLW Chunk generator
public class IRLWChunkGenerator extends ChunkGenerator {
	
	public IRLWChunkGenerator(IWorld worldIn, BiomeProvider biomeProviderIn, GenerationSettings generationSettingsIn) {
		super(worldIn, biomeProviderIn, generationSettingsIn);
		// TODO Auto-generated constructor stub
	}

//TODO Create IRLW Chunk generation Setting
//
	private World world;
	
	private long xDelta;
	private long zDelta;
	private double centerLong;
	private double centerLat;
	private int zoomLevel = 0;
	private TiledMap<MapboxElevationTile> heightMap;
//	
//	private OverworldChunkGenerator overwoldGenerator;
//	
//	private final static int WATER_LEVEL = 63;
//	
//	/**
//	 * @param worldIn
//	 * @param seed
//	 * @param mapFeaturesEnabledIn
//	 * @param generatorOptions
//	 */
//	//TODO Update for new generation
//	public IRLWChunkGenerator(World worldIn, long seed, double centerLong, double centerLat, int zoomLevel) {
//		this.centerLat = centerLat;
//		this.centerLong = centerLong;
//		this.xDelta = (long) WebMercatorUtils.getXFromLongitude(this.centerLong, zoomLevel)/16;
//		this.zDelta = (long) WebMercatorUtils.getYFromLatitude(this.centerLat, zoomLevel)/16;
//		this.xDelta *= 16;	//We are just rounding up to match chunks
//		this.zDelta *= 16;
//		this.zoomLevel = zoomLevel;
//		this.world = worldIn;
//		this.heightMap = new TiledMap<MapboxElevationTile>(TileFactory.MAPBOX_ELEVATION_TILE_FACTORY, this.zoomLevel);
//		this.heightMap.enableSmartLoading();
//		this.overwoldGenerator = new OverworldChunkGenerator(worldIn, seed, true, "");
//	}
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
//	/**
//	 * Gets the terrain height at x and y on the tiled map of the corresponding zoom
//	 * 
//	 * @return the altitude in meter
//	 * @throws InvalidMapboxSessionException 
//	 * @throws IOException 
//	 */
//	private double getHeightFromData(long x, long y) throws IOException, InvalidMapboxSessionException {
//		int[] pixel = this.heightMap.getPixel(x, y);
//		return MapboxUtils.RGBAToHeight(pixel);
//	}
//
//
////TODO Update for new generation
////	@Override
////	public void populate(int x, int z) {
////			this.overwoldGenerator.populate(x, z);
////	}
//
//
////TODO Update for new generation
////	@Override
////	public boolean generateStructures(Chunk chunkIn, int x, int z) {
////		return this.overwoldGenerator.generateStructures(chunkIn, x, z); 
////	}
//
//
//	@Override
//	public List<SpawnListEntry> getPossibleCreatures(EntityClassification creatureType, BlockPos pos) {
//		return this.overwoldGenerator.getPossibleCreatures(creatureType, pos);
//	}
//
//
////TODO Update for new generation
////	@Override
////	public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position,
////			boolean findUnexplored) {
////		return this.overwoldGenerator.getNearestStructurePos(worldIn, structureName, position, findUnexplored);
////	}
//
//
////TODO Update for new generation
////	@Override
////	public void recreateStructures(Chunk chunkIn, int x, int z) {
////		this.overwoldGenerator.recreateStructures(chunkIn, x, z);
////	}
//
////TODO Update for new generation
////	@Override
////	public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
////		return this.overwoldGenerator.isInsideStructure(worldIn, structureName, pos);
////	}
//	
//	
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
