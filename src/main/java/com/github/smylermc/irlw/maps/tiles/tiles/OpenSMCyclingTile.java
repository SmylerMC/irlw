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
import com.github.smylermc.irlw.maps.tiles.RasterWebTile;

/**
 * @author SmylerMC
 *
 * TODO Type comment
 *
 */
public class OpenSMCyclingTile extends RasterWebTile {

	/**
	 * @param size
	 * @param x
	 * @param y
	 * @param zoom
	 */
	public OpenSMCyclingTile(long x, long y, int zoom) {
		super(256, x, y, zoom);
	}

	@Override
	public URL getURL() {
		//TODO API key...
		URL url = null;
		try {
			url = new URL("https://b.tile.thunderforest.com/cycle/" +
					this.zoom + "/" +
					this.x + "/" +
					this.y + ".png");
		} catch (MalformedURLException e) {
			IRLW.logger.error("Failed to craft a valid URL for a tile, please report to the mod author: " + IRLW.AUTHOR_EMAIL);
			IRLW.logger.catching(e);
		}
		return url;
	}

}
