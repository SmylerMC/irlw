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
package com.github.smylermc.irlw.maps.tiles.tiles;

import com.github.smylermc.irlw.maps.tiles.RasterWebTile;

/**
 * @author SmylerMC
 *
 */
public abstract class MapboxWebTile extends RasterWebTile {

	/**
	 * @param size
	 * @param x
	 * @param y
	 * @param zoom
	 * @param defaultPixel
	 */
	public MapboxWebTile(long x, long y, int zoom, int[] defaultPixel) {
		super(256, x, y, zoom, defaultPixel);
	}

	/**
	 * @param x
	 * @param y
	 * @param zoom
	 */
	public MapboxWebTile(long x, long y, int zoom) {
		super(256, x, y, zoom, new int[] {0, 0, 0});
	}

}
