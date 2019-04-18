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

import org.framagit.smylermc.irlw.world.WorldConstants;

import net.minecraft.util.math.BlockPos;

/**
 * A set of methods to work with the web mercator map projection.
 * See https://en.wikipedia.org/wiki/Web_Mercator for more details
 * 
 * @author Smyler
 *
 */
public class WebMercatorUtils {

	
	/* Constants */
	public static final int TILE_DIMENSIONS = 256;
	
	
	/**
	 * 
	 * @param longitude The longitude in degrees, between -180.0 and 180.0
	 * @param zoomLevel The web-mercator zoom level, a positive integer
	 * 
	 * @return The X position corresponding to the given longitude
	 * on a web-mercator map of the given zoom level, with 0;0 being the top left corner of the map.
	 */
	public static double getXFromLongitude(double longitude, int zoomLevel){
		return WebMercatorUtils.getXFromLongitudeRads(Math.toRadians(longitude), zoomLevel);
	}
	
	
	/**
	 * 
	 * @param latitude The latitude in degrees, between -90.0 and 90.0
	 * @param zoomlevel The web-mercator zoom level, a positive integer
	 * 
	 * @return The Y position corresponding to the given latitude
	 * on a web-mercator map of the given zoom level, with 0;0 being the top left corner of the map.
	 */
	public static double getYFromLatitude(double latitude, int zoomlevel){
		return WebMercatorUtils.getYFromLatitudeRads(Math.toRadians(latitude), zoomlevel);
	}
	
	
	/**
	 * 
	 * @param longitude The longitude in radians, between -pi and pi
	 * @param zoomLevel The web-mercator zoom level, a positive integer
	 * 
	 * @return The X position corresponding to the given longitude
	 * on a web-mercator map of the given zoom level, with 0;0 being the top left corner of the map.
	 */
	public static double getXFromLongitudeRads(double longitude, int zoomlevel){
		return ((double)(1 << (zoomlevel + 7))) * (longitude + Math.PI) / Math.PI;
	}
	
	
	/**
	 * 
	 * @param latitude The latitude in radians, between -pi/2 and pi/2
	 * @param zoomlevel The web-mercator zoom level, a positive integer
	 * 
	 * @return The Y position corresponding to the given latitude
	 * on a web-mercator map of the given zoom level, with 0;0 being the top left corner of the map.
	 */
	public static double getYFromLatitudeRads(double lat, int zoomlevel){
		return (128f/Math.PI) * (double)(1 << zoomlevel) * (Math.PI - Math.log(Math.tan( Math.PI / 4  + lat /2)));
	}
	
	
	/** 
	 * @param y a coordinate on a web-mercator map, as an integer with 0;0 as the top left corner
	 * @param zoomLevel The web-mercator zoom level, a positive integer
	 * 
	 * @return The corresponding latitude in degrees, between -180.0 and 180.0
	 */
	public static double getLatitudeFromY(double y, int zoomLevel){
		return Math.toDegrees(WebMercatorUtils.getLatitudeFromYRads(y, zoomLevel));
	}
	
	
	/**
	 * @param x a coordinate on a web-mercator map, as an integer with 0;0 as the top left corner
	 * @param zoomLevel The web-mercator zoom level, a positive integer
	 * 
	 * @return The corresponding longitude in degrees, between -90.0 and 90.0
	 */
	public static double getLongitudeFromX(double x, int zoomLevel){
		return Math.toDegrees(WebMercatorUtils.getLongitudeFromXRads(x, zoomLevel));
	}
	
	
	/** 
	 * @param y a coordinate on a web-mercator map, as an integer with 0;0 as the top left corner
	 * @param zoomLevel The web-mercator zoom level, a positive integer
	 * 
	 * @return The corresponding latitude in radians, between -pi and pi
	 */
	public static double getLatitudeFromYRads(double y, int zoomLevel){
		return 2 * (Math.atan(Math.pow(Math.E, - ((y * Math.PI) / (1 << zoomLevel + 7) - Math.PI))) - Math.PI / 4);		
	}
	
	
	/**
	 * @param x a coordinate on a web-mercator map, as an integer with 0;0 as the top left corner
	 * @param zoomLevel The web-mercator zoom level, a positive integer
	 * 
	 * @return The corresponding longitude in radians, between -pi/2 and pi/2
	 */
	public static double getLongitudeFromXRads(double x, int zoomLevel){
		return (Math.PI * x) / (1 << 7 + zoomLevel) - Math.PI;
	}
	
	
	/**
	 * @param latitude The latitude in radians, between -pi/2 and pi/2
	 * @param zoomLevel The web-mercator zoom level, a positive integer
	 * 
	 * @return an approximate size factor, considering a pixel is a meter
	 */
	public static double getSizeFactorFromLatitudeRads(double latitude, int zoomLevel) {
		return (1<<(zoomLevel+7))/(Math.PI * Math.cos(latitude) * WorldConstants.EARTH_RADIUS);
	}
	
	
	/**
	 * @param y a coordinate on a web-mercator map, as an integer with 0;0 as the top left corner
	 * @param zoomLevel The web-mercator zoom level, a positive integer
	 * 
	 * @return an approximate size factor, considering a pixel is a meter
	 */
	public static double getSizeFactorFromY(long y, int zoomLevel) {
		double latitude = WebMercatorUtils.getLatitudeFromYRads(y, zoomLevel);
		return WebMercatorUtils.getSizeFactorFromLatitudeRads(latitude, zoomLevel);
	}
	
	
	/**
	 * @param latitude The latitude in degrees, between -90.0 and 90.0
	 * @param zoomLevel The web-mercator zoom level, a positive integer
	 * 
	 * @return an approximate size factor, considering a pixel is a meter
	 */
	public static double getSizeFactorFromLatitude(double latitude, int zoomLevel) {
		return (1<<(zoomLevel+7))/(Math.PI * Math.cos(Math.toRadians(latitude)) * WorldConstants.EARTH_RADIUS);
	}
	
	
	/**
	 * 
	 * @param x
	 * @param zoomLevel
	 * @return the x coordinate of the tile at the given x position in the world, assuming map is scaled at zoom level zoomLevel
	 */
	public static long getTileXAt(long x){
		return IRLWUtils.roudSmaller((float)x / WebMercatorUtils.TILE_DIMENSIONS);
	}
	
	/**
	 * 
	 * @param y
	 * @return the y coordinate of the tile at the given z position in the world, assuming map is scaled at zoom level zoomLevel
	 */
	public static long getTileYAt(long y){
		return IRLWUtils.roudSmaller((float)y / WebMercatorUtils.TILE_DIMENSIONS);
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
		return new BlockPos(WebMercatorUtils.mapCoordinatesToWorld(x, zoomLevel), WebMercatorUtils.mapCoordinatesToWorld(y, zoomLevel), 0);
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
		return coordinate - WebMercatorUtils.getMapDimensionInPixel(zoomLevel) / 2;
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
		return new BlockPos(WebMercatorUtils.mapCoordinatesToWorld(x, zoomLevel), mapCoordinatesToWorld(y, zoomLevel), 0);
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
		return coordinate - WebMercatorUtils.getMapDimensionInPixel(zoomLevel) / 2;
	}
	
	
	/**
	 * Returns the size of the map in pixel
	 * 
	 * @param zoomLvl the zoom level of the map to consider
	 * 
	 * @return The size of a side of the map, in pixel
	 */
	public static long getMapDimensionInPixel(int zoomLvl) {
		return WebMercatorUtils.getDimensionsInTile(zoomLvl) * WebMercatorUtils.TILE_DIMENSIONS;
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
	
}
