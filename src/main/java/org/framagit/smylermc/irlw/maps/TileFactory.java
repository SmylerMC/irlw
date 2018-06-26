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
package org.framagit.smylermc.irlw.maps;

import org.framagit.smylermc.irlw.maps.tiles.RasterWebTile;
import org.framagit.smylermc.irlw.maps.tiles.tiles.MapboxElevationTile;
import org.framagit.smylermc.irlw.maps.tiles.tiles.MapboxSatelliteTile;
import org.framagit.smylermc.irlw.maps.tiles.tiles.OpenSMCyclingTile;
import org.framagit.smylermc.irlw.maps.tiles.tiles.WikimediaTile;

/**
 * @author SmylerMC
 *
 */
public interface TileFactory <T extends RasterWebTile>{
	
	public static final TileFactory<MapboxElevationTile> 
	MAPBOX_ELEVATION_TILE_FACTORY = new TileFactory<MapboxElevationTile>() {
		@Override
		public MapboxElevationTile getInstance(long x, long y, int zoom) {
			return new MapboxElevationTile(x, y, zoom);
		}
	};
	
	public static final TileFactory<OpenSMCyclingTile> 
	OPENSM_CYCLING_TILE_FACTORY = new TileFactory<OpenSMCyclingTile>() {
		@Override
		public OpenSMCyclingTile getInstance(long x, long y, int zoom) {
			return new OpenSMCyclingTile(x, y, zoom);
		}
	};
	
	public static final TileFactory<MapboxSatelliteTile> 
	MAPBOX_SATELLITE_TILE_FACTORY = new TileFactory<MapboxSatelliteTile>() {
		@Override
		public MapboxSatelliteTile getInstance(long x, long y, int zoom) {
			return new MapboxSatelliteTile(x, y, zoom);
		}
	};
	
	public static final TileFactory<WikimediaTile> 
	WIKIMEDIA_TILE_FACTORY = new TileFactory<WikimediaTile>() {
		@Override
		public WikimediaTile getInstance(long x, long y, int zoom) {
			return new WikimediaTile(x, y, zoom);
		}
	};


	public T getInstance(long x, long y, int zoom);

}
