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
package com.github.smylermc.irlw.maps;

import java.io.IOException;
import java.util.ArrayList;

import com.github.smylermc.irlw.IRLW;
import com.github.smylermc.irlw.maps.exceptions.InvalidMapboxSessionException;
import com.github.smylermc.irlw.maps.tiles.RasterWebTile;
import com.github.smylermc.irlw.maps.utils.MapboxUtils;

/**
 * @author SmylerMC
 *
 * //TODO class comment
 */
public class TiledMap<T extends RasterWebTile> {

	protected int zoomLevel;
	protected TileFactory<T> factory;
	protected ArrayList<T> tiles;
	
	//TODO javadoc
	public TiledMap(TileFactory<T> fact, int zoomLevel) {
		this.factory = fact;
		this.zoomLevel = zoomLevel;
		this.tiles = new ArrayList<T>();
	}
	
	//TODO javadoc
	public TiledMap(TileFactory<T> fact) {
		this(fact, 0);
	}
	
	
	public int getZoomLevel() {
		return this.zoomLevel;
	}
	
	
	public void setZoomLevel(int zoom) {
		if(zoom < 0) {
			throw new IllegalArgumentException("The zoom level can't be negative!");
		}
		this.zoomLevel = zoom;
	}
	
	
	public T getTile(long x, long y, int zoom) {
		//TODO A better way to store our tiles, this will be very slow with large maps
		for(T tile: this.tiles)
			if(tile.getX() == x && tile.getY() == y && tile.getZoom() == zoom)
				return tile;
		T tile = this.factory.getInstance(x, y, zoom);
		this.tiles.add(tile);
		return tile;
	}
	
	public T getTile(long x, long y) {
		return this.getTile(x, y, this.zoomLevel);
	}
	
	
	public T getTileAt(long x, long y, int zoom) {
		//TODO Throw an exception if tile does not exist
		//TODO Javadoc
		long tileX = MapboxUtils.getTileXAt(x);
		long tileY = MapboxUtils.getTileYAt(y);
		return this.getTile(tileX, tileY, zoom);
	}
	
	//TODO JAvadoc
	public T getTileAt(long x, long y) {
		return this.getTileAt(x, y, this.zoomLevel);
	}
	
	public void unloadTile(T tile) {
		IRLW.logger.error("Trying to unload a tile but the method is not yet implemented");
		//TODO Tile unloading
	}
	
	
	public long getSizeInTiles(){
		return MapboxUtils.getDimensionsInTile(this.zoomLevel);
	}
	
	public long getSizeInPixels(){
		return MapboxUtils.getMapDimensionInPixel(this.zoomLevel);
	}
	
	public int[] getPixel(long x, long y) throws IOException, InvalidMapboxSessionException {
		long tileX = MapboxUtils.getTileXAt(x);
		long tileY = MapboxUtils.getTileYAt(y);
		int tX = (int)(x % 256), tY = (int)(y % 256);
		return this.getTile(tileX, tileY).getPixel(tX, tY);
	}
	
} 	