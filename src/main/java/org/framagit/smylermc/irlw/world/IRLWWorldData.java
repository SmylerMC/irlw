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
*/


package org.framagit.smylermc.irlw.world;

import org.framagit.smylermc.irlw.IRLW;
import org.framagit.smylermc.irlw.world.gen.IRLWChunkGenerator;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;


//FIXME 1.14.4 - IRLWWorldData network and saving
public class IRLWWorldData extends WorldSavedData{
	
	
	//Because only one of this should be saved by world, this is the ID it has to use
	public static final String IRLW_DATA = IRLW.MOD_ID + "_mapboxdata";
	
	
	//Data in question
	private int zoomLevel;
	private long xDelta;
	private long zDelta;
	private double centerLong;
	private double centerLat;
	
	/* The keys to use when storing to NBT */
	private final String KEY_ZOOM = "zoomlevel";
	private final String KEY_LONGITUDE = "long";
	private final String KEY_LATITUDE = "lat";
	private final String KEY_DELTAX = "deltax";
	private final String KEY_DELTAZ = "deltaz";
	
	/**
	 * This is the constructor to use, it sets the good id
	 */
	public IRLWWorldData() {
		super(IRLW_DATA);
		this.markDirty();
		
	}
	
	
	/**
	 * This constructor only exists for forge, use {@link #MapboxWorldData()} if you can
	 * 
	 * @param str This is required by forge, but will be ignored, as there is only one of these per world
	 */
	public IRLWWorldData(String str) {
		this();  
	}
	
	/**
	 * 
	 * @param zoom
	 * @param deltaX
	 * @param deltaY
	 * @param centerLat
	 * @param centerLong
	 * 
	 */
	public IRLWWorldData(int zoom, long deltaX, long deltaZ, double centerLat, double centerLong){
		this();
		this.zoomLevel = zoom;
		this.xDelta = deltaX;
		this.zDelta = deltaZ;
		this.centerLat = centerLat;
		this.centerLong = centerLong;
		this.markDirty();
	}


	@Override
	public void read(CompoundNBT nbt) {
		this.zoomLevel = nbt.getInt(this.KEY_ZOOM);
		this.centerLat = nbt.getDouble(this.KEY_LATITUDE);
		this.centerLong = nbt.getDouble(this.KEY_LONGITUDE);
		this.xDelta = nbt.getLong(this.KEY_DELTAX);
		this.zDelta = nbt.getLong(this.KEY_DELTAZ);
//		this.isBorderSet = nbt.getBoolean("borderset");
		this.markDirty();
	}

	
	/**
	 * Writes the values to an nbt tag
	 * 
	 * @param nbt the nbt tag to write
	 */
	@Override
	public CompoundNBT write(CompoundNBT nbt) {
		nbt.putInt(this.KEY_ZOOM, this.zoomLevel);
		nbt.putDouble(this.KEY_LATITUDE, this.centerLat);
		nbt.putDouble(this.KEY_LONGITUDE, this.centerLong);
		nbt.putLong(this.KEY_DELTAX, this.xDelta);
		nbt.putLong(this.KEY_DELTAZ, this.zDelta);
//		nbt.setBoolean("borderset", this.isBorderSet);
		return nbt;
	}

	
	public static IRLWWorldData fromBytes(PacketBuffer buf) {
		//Order should be the same as in this#toBytes(ByteBuf buf)
		IRLWWorldData data = new IRLWWorldData();
		data.setZoomLevel(buf.readInt());
		data.setCenterLatitude(buf.readDouble());
		data.setCenterLongitude(buf.readDouble());
		data.setDeltaX(buf.readLong());
		data.setDeltaZ(buf.readLong());
//		data.isBorderSet = buf.readBoolean();
		return data;
	}

	
	public static void toBytes(IRLWWorldData data, PacketBuffer buf) {
		//Order should be the same as in this#fromBytes(ByteBuf buf)
		buf.writeInt(data.getZoomLevel());
		buf.writeDouble(data.getCenterLatitude());
		buf.writeDouble(data.getCenterLongitude());
		buf.writeLong(data.getDeltaX());
		buf.writeLong(data.getDeltaZ());
//		buf.writeBoolean(this.isBorderSet);
	}

	
	/**
	 * Sets the world data for the given world using the generators' json
	 * Does nothing if the world is not an irlw world
	 * 
	 * @warning Should be called on a server world
	 * 
	 * @param world the world to set
	 */
	public static void setForWorld(ServerWorld world) {
		if(! world.getWorldType().getName().equals(IRLW.WORLD_TYPE_NAME)) {
			return;
		}
		ServerChunkProvider provider = (ServerChunkProvider) world.getChunkProvider();
		IRLWChunkGenerator generator = (IRLWChunkGenerator) provider.generator;
		IRLWWorldData data = new IRLWWorldData();
		data.setCenterLongitude(generator.getCenterLongitude());
		data.setCenterLatitude(generator.getCenterLatitude());
		data.setDeltaX(generator.getDeltaX());
		data.setDeltaZ(generator.getDeltaZ());
		data.setZoomLevel(generator.getZoomLevel());
		world.getSavedData().set(data); //FIXME 1.14.4 Make sure that works
	}
	
	
//	/**
//	 * The packet handler for a IRLWWorldData object
//	 * 
//	 * @author Smyler
//	 *
//	 */
//	public static class IRLWWorldDataMessageHandler implements Runnable {
//
//		//Required by forge
//		public IRLWWorldDataMessageHandler(){}
//		
//		
//		//Used to set the data from Minecraft thread
//		private IRLWWorldData dataToSet = null;
//		
//
//		/**
//		 * Called from the client network thread when receiving a MapboxWorldData packet
//		 */
//		@Override
//		public IMessage onMessage(IRLWWorldData message, MessageContext ctx) {
//			if(ctx.side.equals(Side.CLIENT)){
//				IRLW.logger.debug("Received mapbox mapdata from server.");
//				this.dataToSet = message;
//				Minecraft.getMinecraft().addScheduledTask(this);
//			}
//		    return null;
//		}
//		
//		
//		/**
//		 * Should be called from the Minecraft thread
//		 */
//		@Override
//		public void run() {
//			if(dataToSet == null){
//				IRLW.logger.error("Calling run to set mapbox world data but it is null! Please report this error at " + IRLW.AUTHOR_EMAIL);
//				return;
//			}
//			if(! Minecraft.getInstance().isCallingFromMinecraftThread()){
//				IRLW.logger.error("Calling run to set mapbox world data from thread " + Thread.currentThread().getName() + "! This is not normal, please report at " + IRLW.AUTHOR_EMAIL);
//				return;
//			}
//			try{
//				World world = Minecraft.getInstance().world;
//				world.setData(IRLWWorldData.IRLW_DATA, dataToSet);
//				IRLW.logger.info("Successfully synchronised mapbox map data with server.");
//			}catch(Exception e){
//				IRLW.logger.error("Failed to set mapbox mapdata, please report this error at " + IRLW.AUTHOR_EMAIL);
//				IRLW.logger.catching(e);
//			}
//		}
//	}
	
	
	/* Getters and Setters from there */
	
	
	/**
	 * 
	 * @param zoom
	 * 
	 */
	public void setZoomLevel(int zoom){
		this.zoomLevel = zoom;
		this.markDirty();
	}
	
	/**
	 * 
	 * @return The zoom level of the world
	 */
	public int getZoomLevel(){
		return this.zoomLevel;
	}
	
	
	//TODO Implement the world borders
//	/**
//	 * Considers the world border as set for the world. Used to only set them once per world,
//	 * to allow custom player settings thought the /worldborder command
//	 */
//	public void borderSet(){
//		this.isBorderSet = true;
//		this.markDirty();
//	}
	
//	/**
//	 * 
//	 * @return True if this {@link #borderSet()} has been called
//	 */
//	public boolean isBorderSet(){
//		return this.isBorderSet;
//	}
	
	
	public long getDeltaX() {
		return xDelta;
	}


	public void setDeltaX(long xDelta) {
		this.xDelta = xDelta;
		this.markDirty();
	}


	public long getDeltaZ() {
		return zDelta;
	}


	public void setDeltaZ(long zDelta) {
		this.zDelta = zDelta;
		this.markDirty();
	}


	public double getCenterLongitude() {
		return centerLong;
	}


	public void setCenterLongitude(double centerLong) {
		this.centerLong = centerLong;
		this.markDirty();
	}


	public double getCenterLatitude() {
		return centerLat;
	}


	public void setCenterLatitude(double centerLat) {
		this.centerLat = centerLat;
		this.markDirty();
	}
	
	
}