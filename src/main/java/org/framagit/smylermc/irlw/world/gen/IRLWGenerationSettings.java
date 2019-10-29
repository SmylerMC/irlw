package org.framagit.smylermc.irlw.world.gen;

import org.framagit.smylermc.irlw.IRLWConfiguration;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.gen.GenerationSettings;

public class IRLWGenerationSettings extends GenerationSettings{

	private static final String ZOOM_LEVEL_KEY = "zoom_level";
	public static final int MAX_ZOOM_LEVEL = 15;
	protected int zoomLevel = IRLWConfiguration.defaultZoomLevel.get();
	
	private static final String SPAWN_LAT_KEY = "spawn_lat";
	protected double spawnLat = 0.0D;
	
	private static final String SPAWN_LONG_KEY = "spawn_long";
	protected double spawnLong = 0.0D;
	
	private static final String INFINITE_OCEAN_KEY = "infinite_ocean";
	protected boolean infinitOcean = false;
	
	private static final String MAKE_BEDROCK_KEY = "make_bedrock";
	protected boolean makeBedrock = true;
	
	public IRLWGenerationSettings() {	
	}
	
	public IRLWGenerationSettings(CompoundNBT nbt) throws InvalidSettingsException {
		if(nbt.contains(ZOOM_LEVEL_KEY)) this.setZoomLevel(nbt.getInt(ZOOM_LEVEL_KEY));
		if(nbt.contains(SPAWN_LAT_KEY)) this.setSpawnLat(nbt.getDouble(SPAWN_LAT_KEY));
		if(nbt.contains(SPAWN_LONG_KEY)) this.setSpawnLong(nbt.getDouble(SPAWN_LONG_KEY));
		if(nbt.contains(INFINITE_OCEAN_KEY)) this.setInfiniteOcean(nbt.getBoolean(INFINITE_OCEAN_KEY));
		if(nbt.contains(MAKE_BEDROCK_KEY)) this.setMakeBedrock(nbt.getBoolean(MAKE_BEDROCK_KEY));
	}
	
	public IRLWGenerationSettings(String json) throws InvalidSettingsException {
		JsonParser parser = new JsonParser();
    	JsonObject jsonObject = parser.parse(json).getAsJsonObject();
    	if(jsonObject.has(ZOOM_LEVEL_KEY)) this.setZoomLevel(jsonObject.get(ZOOM_LEVEL_KEY).getAsInt());
    	if(jsonObject.has(SPAWN_LONG_KEY)) this.setSpawnLong(jsonObject.get(SPAWN_LONG_KEY).getAsDouble());
    	if(jsonObject.has(SPAWN_LAT_KEY)) this.setSpawnLat(jsonObject.get(SPAWN_LAT_KEY).getAsDouble());
    	if(jsonObject.has(INFINITE_OCEAN_KEY)) this.setInfiniteOcean(jsonObject.get(INFINITE_OCEAN_KEY).getAsBoolean());
    	if(jsonObject.has(MAKE_BEDROCK_KEY)) this.setMakeBedrock(jsonObject.get(MAKE_BEDROCK_KEY).getAsBoolean());
	}
	
	public CompoundNBT toNBT() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putInt(ZOOM_LEVEL_KEY, this.getZoomLevel());
		nbt.putDouble(SPAWN_LAT_KEY, this.getSpawnLat());
		nbt.putDouble(SPAWN_LONG_KEY, this.getSpawnLong());
		nbt.putBoolean(INFINITE_OCEAN_KEY, this.getInfiniteOcean());
		nbt.putBoolean(MAKE_BEDROCK_KEY, this.getMakeBedrock());
		return nbt;
	}
	
	public String toJson() {
		//TODO Do not hard-code JSON generation
		return "{\"" + ZOOM_LEVEL_KEY + "\": " + 
					this.getZoomLevel() + 
				", \"" + SPAWN_LONG_KEY + "\": " +
					this.getSpawnLong() + 
				", \"" + SPAWN_LAT_KEY + "\": " + 
					this.getSpawnLat() + 
				", \"" + INFINITE_OCEAN_KEY + "\": " +
					this.getInfiniteOcean() +
				", \"" + MAKE_BEDROCK_KEY + "\": " +
					this.getMakeBedrock() +
				"}";
	}

	public int getZoomLevel() {
		return zoomLevel;
	}

	public void setZoomLevel(int zoomLevel) throws InvalidSettingsException {
		if(zoomLevel < 0 || zoomLevel > MAX_ZOOM_LEVEL) throw new InvalidSettingsException();
		this.zoomLevel = zoomLevel;
	}

	public double getSpawnLat() {
		return spawnLat;
	}

	public void setSpawnLat(double spawnLat) throws InvalidSettingsException {
		if(Math.abs(spawnLat) > 85D) throw new InvalidSettingsException();
		this.spawnLat = spawnLat;
	}

	public double getSpawnLong() {
		return spawnLong;
	}

	public void setSpawnLong(double spawnLong) throws InvalidSettingsException {
		if(Math.abs(spawnLong) > 180D) throw new InvalidSettingsException();
		this.spawnLong = spawnLong;
	}
	
	public boolean getInfiniteOcean() {
		return this.infinitOcean;
	}
	
	public void setInfiniteOcean(boolean infinitSea) {
		this.infinitOcean = infinitSea;
	}
	
	public boolean getMakeBedrock() {
		return makeBedrock;
	}

	public void setMakeBedrock(boolean makeBedrock) {
		this.makeBedrock = makeBedrock;
	}

    @Override
    public int getBedrockFloorHeight() {
            return this.getMakeBedrock()?0:256;
    }

	public class InvalidSettingsException extends Exception {

		private static final long serialVersionUID = -6903460846705201168L;
		
		
	}
	
}
