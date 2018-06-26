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
package org.framagit.smylermc.irlw.maps.tiles.tiles;

import java.net.MalformedURLException;
import java.net.URL;

import org.framagit.smylermc.irlw.IRLW;
import org.framagit.smylermc.irlw.IRLWConfiguration;
import org.framagit.smylermc.irlw.maps.TileFactory;

/**
 * @author SmylerMC
 *
 */
public class MapboxElevationTile extends MapboxWebTile implements TileFactory<MapboxElevationTile>{

	/**
	 * @param x
	 * @param y
	 * @param zoom
	 * @param defaultPixel
	 */
	public MapboxElevationTile(long x, long y, int zoom) {
		super(x, y, zoom, new int[]{1, 134, 150});
	}

	@Override
	public URL getURL() {
		try {
			return new URL("https://api.mapbox.com/v4/mapbox.terrain-rgb/"
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

	@Override
	public MapboxElevationTile getInstance(long x, long y, int zoom) {
		return new MapboxElevationTile(x, y, zoom);
	}

}
