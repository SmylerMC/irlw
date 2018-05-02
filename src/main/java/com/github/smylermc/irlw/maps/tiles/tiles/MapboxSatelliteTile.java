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

	File created on 2 mars 2018 
*/
package com.github.smylermc.irlw.maps.tiles.tiles;

import java.net.MalformedURLException;
import java.net.URL;

import com.github.smylermc.irlw.IRLW;
import com.github.smylermc.irlw.IRLWConfiguration;

/**
 * @author SmylerMC
 *
 * TODO Type comment
 *
 */
public class MapboxSatelliteTile extends MapboxWebTile {

	/**
	 * @param x
	 * @param y
	 * @param zoom
	 * @param defaultPixel
	 */
	public MapboxSatelliteTile(long x, long y, int zoom) {
		super(x, y, zoom);
	}


	@Override
	public URL getURL() {
		try {
			//TODO HTTPS, changed for debuging
			return new URL("http://api.mapbox.com/v4/mapbox.satellite/"
					+ this.getZoom() + "/"
					+ this.getX() + "/"
					+ this.getY() + ".png?"
					+ "access_token="
					+ IRLWConfiguration.mapboxToken);
		} catch (MalformedURLException e) {
			IRLW.logger.error("A terrain elevation tile has a malformed URL. Please report the bug at " + IRLW.AUTHOR_EMAIL);
			IRLW.logger.error("Returning null and hopping the the best... See stacktrace:");
			IRLW.logger.catching(e);
			return null;
		}
	}

}
