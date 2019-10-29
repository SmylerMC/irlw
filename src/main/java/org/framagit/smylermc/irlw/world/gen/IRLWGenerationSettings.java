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
	
	private static final String INFINIT_OCEAN_KEY = "infinit_ocean";
	protected boolean infinitOcean = false;
	
	public IRLWGenerationSettings() {	
	}
	
	public IRLWGenerationSettings(CompoundNBT nbt) throws InvalidSettingsException {
		this.setZoomLevel(nbt.getInt(ZOOM_LEVEL_KEY));
		this.setSpawnLat(nbt.getDouble(SPAWN_LAT_KEY));
		this.setSpawnLong(nbt.getDouble(SPAWN_LONG_KEY));
		this.setInfinitOcean(nbt.getBoolean(INFINIT_OCEAN_KEY));
	}
	
	public IRLWGenerationSettings(String json) throws InvalidSettingsException {
		JsonParser parser = new JsonParser();
    	JsonObject jsonObject = parser.parse(json).getAsJsonObject();
    	this.setZoomLevel(jsonObject.get(ZOOM_LEVEL_KEY).getAsInt());
    	this.setSpawnLong(jsonObject.get(SPAWN_LONG_KEY).getAsDouble());
    	this.setSpawnLat(jsonObject.get(SPAWN_LAT_KEY).getAsDouble());
    	this.setInfinitOcean(jsonObject.get(INFINIT_OCEAN_KEY).getAsBoolean());
	}
	
	public CompoundNBT toNBT() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putInt(ZOOM_LEVEL_KEY, this.getZoomLevel());
		nbt.putDouble(SPAWN_LAT_KEY, this.getSpawnLat());
		nbt.putDouble(SPAWN_LONG_KEY, this.getSpawnLong());
		nbt.putBoolean(INFINIT_OCEAN_KEY, this.getInfinitOcean());
		return nbt;
	}
	
	public String toJson() {
		//TODO Do not hard-code JSON generation
		return "{\"" + ZOOM_LEVEL_KEY + "\": " + 
					this.zoomLevel + 
				", \"" + SPAWN_LONG_KEY + "\": " +
					this.spawnLong + 
				", \"" + SPAWN_LAT_KEY + "\": " + 
					this.spawnLat + 
				", \"" + INFINIT_OCEAN_KEY + "\":" +
					this.infinitOcean +
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
	
	public boolean getInfinitOcean() {
		return this.infinitOcean;
	}
	
	public void setInfinitOcean(boolean infinitSea) {
		this.infinitOcean = infinitSea;
	}
	
	public class InvalidSettingsException extends Exception {

		private static final long serialVersionUID = -6903460846705201168L;
		
		
	}
	
}
