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


package org.framagit.smylermc.irlw.maps.utils;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.framagit.smylermc.irlw.IRLW;
import org.framagit.smylermc.irlw.maps.exceptions.InvalidMapboxSessionException;

import net.minecraft.util.math.BlockPos;


/**
 * A set of methods to work with Mapbox maps
 * 
 * 
 * @author Smyler
 *
 */
public class MapboxUtils {
	
	
	/* Constants */
	public static final int TILE_DIMENSIONS = 256;
	
	
	/**
	 * Convert a the RGB value of a pixel to a height using Mapbox's base 255 height system.
	 * See https://www.mapbox.com/blog/terrain-rgb/ for more info
	 * 
	 * @param pixel An array of ints: {int red, int green, int blue[, int alpha]} (Alpha is not required)
	 * 
	 * @return The height in meters
	 */
	public static float RGBAToHeight(int[] pixel){
		return (float) (-10000 + (pixel[0] * 256 * 256 + pixel[1] * 256 + pixel[2]) * 0.1);
	}
	
	
	/**
	 * 
	 * @param x
	 * @param zoomLevel
	 * @return the x coordinate of the tile at the given x position in the world, assuming map is scaled at zoom level zoomLevel
	 */
	public static long getTileXAt(long x){
		return IRLWUtils.roudSmaller((float)x / MapboxUtils.TILE_DIMENSIONS);
	}
	
	/**
	 * 
	 * @param y
	 * @return the y coordinate of the tile at the given z position in the world, assuming map is scaled at zoom level zoomLevel
	 */
	public static long getTileYAt(long y){
		return IRLWUtils.roudSmaller((float)y / MapboxUtils.TILE_DIMENSIONS);
	}
	
	
	/**
	 * 
	 * @param x
	 * @param z
	 * @param zoom
	 * @return true if the block at the given x and z is on the real world scaled at zoom
	 */
	public static boolean isInWorld(long x, long z, int zoom){
		long tX = getTileXAt(x);
		long tY = getTileYAt(z);
		int mS = 1 << zoom;
		return tX >= 0 && tX < mS && tY >= 0 && tY < mS;
	}
	
	
	/**
	 * 
	 * @param x
	 * @param z
	 * @param zoom
	 * @return true if the tile at given x and z is on the real world scaled at zoom
	 */
	public static boolean isTileInWorld(long tX, long tY, int zoom){
		int mS = 1 << zoom;
		return tX >= 0 && tX < mS && tY >= 0 && tY < mS;
	}
	
	
	/**
	 * Simply translates the coordinates from a web map which as 0;0 on it upper left corner to a Minecraft world BlockPos,
	 * considering Latitude 0 and Longitude 0 are at x=0 and z=0.
	 * It basically just removes half of the map's size to x and y.
	 * 
	 * @param x The x coordinate of a pixel in a map
	 * @param y The y coordinate of a pixel in a map
	 * @param zoomLevel The web mercator zoom level of the map
	 * @return a BlockPos object which x and z coordinates matches the translated x and y map coordinates to the world. The BlockPos y is always 0.
	 */
	public static BlockPos mapCoordinatesToWorld(long x, long y, int zoomLevel) {
		return new BlockPos(mapCoordinatesToWorld(x, zoomLevel), mapCoordinatesToWorld(y, zoomLevel), 0);
	}
	
	
	/**
	 * Simply translates the coordinates from a web map which as 0;0 on it upper left corner to a Minecraft world BlockPos,
	 * considering Latitude 0 and Longitude 0 are at x=0 and z=0.
	 * It basically just removes half of the map's size to x and y.
	 * 
	 * @param coordinate the coordinate to translate. The result is the same for x or y
	 * @param zoomLevel The web mercator zoom level of the map
	 * 
	 * @return a BlockPos object which x and z coordinates matches the translated x and y map coordinates to the world. The BlockPos y is always 0.
	 * 
	 * @deprecated
	 */
	public static long mapCoordinatesToWorld(long coordinate,int zoomLevel) {
		return coordinate - MapboxUtils.getMapDimensionInPixel(zoomLevel) / 2;
	}
	
	
	/**
	 * Simply translates the coordinates from a web map which as 0;0 on it upper left corner to a Minecraft world BlockPos,
	 * considering Latitude 0 and Longitude 0 are at x=0 and z=0.
	 * It basically just removes half of the map's size to x and y.
	 * 
	 * @param x The x coordinate of a pixel in a map
	 * @param y The y coordinate of a pixel in a map
	 * @param zoomLevel The web mercator zoom level of the map
	 * 
	 * @return a BlockPos object which x and z coordinates matches the translated x and y map coordinates to the world. The BlockPos y is always 0.
	 */
	public static BlockPos mapCoordinatesToWorld(double x, double y, int zoomLevel) {
		return new BlockPos(mapCoordinatesToWorld(x, zoomLevel), mapCoordinatesToWorld(y, zoomLevel), 0);
	}
	
	
	/**
	 * Simply translates the coordinates from a web map which as 0;0 on it upper left corner to a Minecraft world BlockPos,
	 * considering Latitude 0 and Longitude 0 are at x=0 and z=0.
	 * It basically just removes half of the map's size to x and y.
	 * 
	 * @param coordinate the coordinate to translate. The result is the same for x or y
	 * @param zoomLevel The web mercator zoom level of the map
	 * 
	 * @return a BlockPos object which x and z coordinates matches the translated x and y map coordinates to the world. The BlockPos y is always 0.
	 */
	public static double mapCoordinatesToWorld(double coordinate,int zoomLevel) {
		return coordinate - MapboxUtils.getMapDimensionInPixel(zoomLevel) / 2;
	}
	
	
	/**
	 * Saves the given URL to the given File, via http and interprets the response code according to mapbox's codes.
	 * 
	 * @param url
	 * @param file
	 * @throws IOException
	 * @throws InvalidMapboxSessionException
	 */
	public static void saveMapboxUrlToFile(URL url, File file) throws IOException, InvalidMapboxSessionException {
		int response = IRLW.cacheManager.downloadUrlToFile(url, file);
		if(response == HttpURLConnection.HTTP_UNAUTHORIZED) {
			throw new InvalidMapboxSessionException();
		}
	}
	
	
	/**
	 * Returns the size of the map in pixel
	 * 
	 * @param zoomLvl the zoom level of the map to consider
	 * 
	 * @return The size of a side of the map, in pixel
	 */
	public static long getMapDimensionInPixel(int zoomLvl) {
		return MapboxUtils.getDimensionsInTile(zoomLvl) * MapboxUtils.TILE_DIMENSIONS;
	}
	
	
	/**
	 * Returns the length of a side of a map of the given  zoom level, in tile
	 * It is simply 2 raised to the power of the zoom
	 * 
	 * @param zoomlvl the zoom level of the map to consider
	 * @return 2^zoomlvl
	 */
	public static long getDimensionsInTile(int zoomlvl) {
		return 1 << zoomlvl;
	}
	
	
	/**
	 * Checks if the given token is valid.
	 * 
	 * @param token
	 * n@return true if the token is valid
	 */
	public static boolean checkToken(String token) {
		try {
			URL url = new URL("https://a.tiles.mapbox.com/v4/mapbox.satellite/0/0/0.png?access_token=" + token);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("HEAD");  //Only need the header to see if we are authorized
			connection.setDefaultUseCaches(false);
			connection.connect();
			if(connection.getResponseCode() == 200 || connection.getResponseCode() == 304) {
				return true;
			}
			return false;
		} catch (Exception e) {
			IRLW.logger.error("Internal error while checking token. Please inform the modder at " + IRLW.AUTHOR_EMAIL);
			IRLW.logger.catching(e);
		}
		return false;
	}

	
}
