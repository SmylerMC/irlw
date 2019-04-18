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


/**
 * A set of methods to work with Mapbox maps
 * 
 * 
 * @author Smyler
 *
 */
public class MapboxUtils {
	
	
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
