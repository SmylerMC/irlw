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


package com.github.smylermc.irlw.maps.utils;


/**
 * A set of methods to work with the web mercator map projection.
 * See https://en.wikipedia.org/wiki/Web_Mercator for more details
 * 
 * @author Smyler
 *
 */
public class WebMercatorUtils {

	
	/**
	 * 
	 * @param lo The longitude in degrees
	 * @param zoomlevel The zoom level
	 * @return a double
	 */
	public static double getXFromLongitude(double lo, int zoomlevel){
		return WebMercatorUtils.getXFromLongitudeRads(Math.toRadians(lo), zoomlevel);
	}
	
	
	/**
	 * 
	 * @param lat The latitude in rads
	 * @param zoomlevel The zoom level
	 * @return a double
	 */
	public static double getYFromLatitude(double lat, int zoomlevel){
		return WebMercatorUtils.getYFromLatitudeRads(Math.toRadians(lat), zoomlevel);
	}
	
	
	/**
	 * 
	 * @param lo The longitude in rads
	 * @param zoomlevel The zoom level
	 * @return a double
	 */
	public static double getXFromLongitudeRads(double lo, int zoomlevel){
		return ((double)(1 << (zoomlevel + 7))) * (lo + Math.PI) / Math.PI;
	}
	
	
	/**
	 * 
	 * @param lat The latitude in rads
	 * @param zoomlevel The zoom level
	 * @return a double
	 */
	public static double getYFromLatitudeRads(double lat, int zoomlevel){
		return (128f/Math.PI) * (double)(1 << zoomlevel) * (Math.PI - Math.log(Math.tan( Math.PI / 4  + lat /2)));
	}
	
	
	/**
	 * Returns the latitude of a given y point of the map
	 * 
	 * @param y
	 * @param zoomLevel
	 * 
	 * @return The latitude in degrees
	 */
	public static double getLatFromY(double y, int zoomLevel){
		return Math.toDegrees(WebMercatorUtils.getLatFromYRads(y, zoomLevel));
	}
	
	
	/**
	 * Returns the longitude of a given x point of the map
	 * 
	 * @param y
	 * @param zoomLevel
	 * 
	 * @return The longitude in degrees
	 */
	public static double getLongFromX(double x, int zoomLevel){
		return Math.toDegrees(WebMercatorUtils.getLongFromXRads(x, zoomLevel));
	}
	
	
	/**
	 * Returns the latitude of a given y point of the map
	 * 
	 * @param y
	 * @param zoomLevel
	 * 
	 * @return The latitude in radians
	 */
	public static double getLatFromYRads(double y, int zoomLevel){
		return 2 * (Math.atan(Math.pow(Math.E, - ((y * Math.PI) / (1 << zoomLevel + 7) - Math.PI))) - Math.PI / 4);
		
		
	}
	
	
	/**
	 * Returns the longitude of a given x point of the map
	 * 
	 * @param y
	 * @param zoomLevel
	 * 
	 * @return The longitude in radians
	 */
	public static double getLongFromXRads(double x, int zoomLevel){
		return (Math.PI * x) / (1 << 7 + zoomLevel) - Math.PI;
	}
	
}
