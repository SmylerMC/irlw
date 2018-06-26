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
	 * @param y a coordinate, as a positive integer, with 0;0 as the top left corner
	 * @param zoomLevel The web-mercator zoom level, a positive integer
	 * 
	 * @return The corresponding latitude in degrees, between -180.0 and 180.0
	 */
	public static double getLatFromY(double y, int zoomLevel){
		return Math.toDegrees(WebMercatorUtils.getLatFromYRads(y, zoomLevel));
	}
	
	
	/**
	 * @param x a coordinate, as a positive integer, with 0;0 as the top left corner
	 * @param zoomLevel The web-mercator zoom level, a positive integer
	 * 
	 * @return The corresponding longitude in degrees, between -90.0 and 90.0
	 */
	public static double getLongFromX(double x, int zoomLevel){
		return Math.toDegrees(WebMercatorUtils.getLongFromXRads(x, zoomLevel));
	}
	
	
	/** 
	 * @param y a coordinate, as a positive integer, with 0;0 as the top left corner
	 * @param zoomLevel The web-mercator zoom level, a positive integer
	 * 
	 * @return The corresponding latitude in radians, between -pi and pi
	 */
	public static double getLatFromYRads(double y, int zoomLevel){
		return 2 * (Math.atan(Math.pow(Math.E, - ((y * Math.PI) / (1 << zoomLevel + 7) - Math.PI))) - Math.PI / 4);		
	}
	
	
	/**
	 * @param x a coordinate, as a positive integer, with 0;0 as the top left corner
	 * @param zoomLevel The web-mercator zoom level, a positive integer
	 * 
	 * @return The corresponding longitude in radians, between -pi/2 and pi/2
	 */
	public static double getLongFromXRads(double x, int zoomLevel){
		return (Math.PI * x) / (1 << 7 + zoomLevel) - Math.PI;
	}
	
}
