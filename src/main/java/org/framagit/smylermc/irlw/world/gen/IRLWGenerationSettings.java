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
	
	public IRLWGenerationSettings() {	
	}
	
	public IRLWGenerationSettings(CompoundNBT nbt) throws InvalidSettingsException {
		this.setZoomLevel(nbt.getInt(ZOOM_LEVEL_KEY));
		this.setSpawnLat(nbt.getDouble(SPAWN_LAT_KEY));
		this.setSpawnLong(nbt.getDouble(SPAWN_LONG_KEY));
	}
	
	public IRLWGenerationSettings(String json) throws InvalidSettingsException {
		JsonParser parser = new JsonParser();
    	JsonObject jsonObject = parser.parse(json).getAsJsonObject();
    	this.setZoomLevel(jsonObject.get("zoomlevel").getAsInt());
    	this.setSpawnLong(jsonObject.get("spawn_long").getAsDouble());
    	this.setSpawnLat(jsonObject.get("spawn_lat").getAsDouble());
	}
	
	public CompoundNBT toNBT() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putInt(ZOOM_LEVEL_KEY, this.zoomLevel);
		nbt.putDouble(SPAWN_LAT_KEY, this.spawnLat);
		nbt.putDouble(SPAWN_LONG_KEY, spawnLong);
		return nbt;
	}
	
	public String toJson() {
		//TODO Do not hard-code JSON generation
		return "{\"zoomlevel\": " + this.zoomLevel + ", \"spawn_long\": " + this.spawnLong + ", \"spawn_lat\": " + this.spawnLat + "}";
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

	public class InvalidSettingsException extends Exception {

		private static final long serialVersionUID = -6903460846705201168L;
		
		
	}
	
}
