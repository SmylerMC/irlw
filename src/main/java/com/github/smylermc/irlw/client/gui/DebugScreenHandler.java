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


package com.github.smylermc.irlw.client.gui;

import java.util.ArrayList;

import com.github.smylermc.irlw.IRLW;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;


/**
 * A class that can provide useful information for the debug screen. Will crash on dedicated server.
 * 
 * @author Smyler
 *
 */
public final class DebugScreenHandler {
	
	
	private static final String PREFIX = TextFormatting.DARK_GREEN + "[IRLW] " + TextFormatting.RESET;
	
	
	//Used to avoid spamming the logs if the rendering fails again and again.
	private boolean isRenderingExceptionLogged = false;
	
	
	/**
	 * 
	 * @return The debug lines to add on the right of the screen.
	 */
	public ArrayList<String> getRight(){
		ArrayList<String> list = new ArrayList<String>();
		if(! Minecraft.getMinecraft().world.getWorldType().getName().equals(IRLW.WORLD_TYPE_NAME)){
			return list;
		}
		return list;
	}
	
	
	/**
	 * 
	 * @return The debug lines to add on the left of the screen.
	 */
	public ArrayList<String> getLeft(){
		ArrayList<String> list = new ArrayList<String>();
		try{
			//TODO
//			World world = Minecraft.getMinecraft().world;
//			if((!world.getWorldType().getName().equals(IRLW.WORLD_TYPE_NAME)) || world.getGameRules().getBoolean("reducedDebugInfo")) //TODO The gamerules doesn't seem to be sync :(
//				return list;
//			list.add("");
//			EntityPlayerSP player = Minecraft.getMinecraft().player;
//			MapboxWorldData mapbox = (MapboxWorldData)world.loadData(MapboxWorldData.class, MapboxWorldData.MAPBOX_DATA);
//			int zoom = mapbox.getZoomLevel();
//			double playerX = player.getPosition().getX();
//			double playerZ = player.getPosition().getZ();
//			if(MapboxUtils.isInWorld((long)playerX, (long)playerZ, zoom)){
//				list.add(PREFIX + "Mercator zoom: " + zoom + "; Tile: x=" + MapboxUtils.getTileXAt((long) playerX, zoom) + "; y=" + MapboxUtils.getTileYAt((long) playerZ, zoom));
//				list.add(PREFIX + "Latitude: " + WebMercatorUtils.getLatFromY(playerZ + (1<<zoom)*MapboxMap.TILE_DIMENSIONS/2, zoom)+ "°, Longitude: " + WebMercatorUtils.getLongFromX(playerX + (1<<zoom)*MapboxMap.TILE_DIMENSIONS/2, zoom) + "°");
//			}else{
//				list.add(PREFIX + "Mercator zoom: " + zoom + ";" + TextFormatting.YELLOW + " You are outside the world." + TextFormatting.RESET);
//			}
			if(Minecraft.getMinecraft().isIntegratedServerRunning())
				list.add(PREFIX + "Cacher queue size: " + IRLW.cacheManager.getQueueSize());
		}catch(Exception e){
			list.add(PREFIX + TextFormatting.RED + "IRLW can't display its debug information because of an internal error." + TextFormatting.RESET);
			list.add(PREFIX + TextFormatting.RED + "Please tell the modder at " + IRLW.AUTHOR_EMAIL + TextFormatting.RESET);
			list.add(PREFIX + TextFormatting.RED + "Exception: " + e.toString() + ":" + e.getMessage() + TextFormatting.RESET);
			if(!this.isRenderingExceptionLogged){
				IRLW.logger.error("Failed to display F3 debug information. Please report to th modder at " + IRLW.AUTHOR_EMAIL);
				IRLW.logger.catching(e);
				this.isRenderingExceptionLogged = true;
			}
		}
		return list;
	}
	
	
}