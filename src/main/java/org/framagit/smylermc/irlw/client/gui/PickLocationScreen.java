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

	File created on 8 avr. 2018 
*/
package org.framagit.smylermc.irlw.client.gui;

import org.framagit.smylermc.irlw.client.gui.widget.TiledMapWidget;
import org.framagit.smylermc.irlw.maps.TileFactory;
import org.framagit.smylermc.irlw.maps.TiledMap;
import org.framagit.smylermc.irlw.maps.tiles.tiles.MapboxSatelliteTile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * @author SmylerMC
 *
 * TODO Type comment
 *
 */
public class PickLocationScreen extends Screen {

	protected Minecraft minecraft;
	protected TiledMapWidget map;
	protected Screen parent;
	
	public PickLocationScreen(Screen parent) {
		super(new TranslationTextComponent("")); //FIXME 1.14.4 - pick location screen Title
		//TODO Localization for pick location screen map widget
		this.map = new TiledMapWidget(0, 0, this.width, this.height, "Pick a location", new TiledMap<MapboxSatelliteTile>(TileFactory.MAPBOX_SATELLITE_TILE_FACTORY));
		this.addButton(this.map);
		this.parent = parent;
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		// this.drawDefaultBackground(); //FIXME 1.14.4 - Check if not necessary
		//this.map.render(); //FIXME 1.14.4 - Make sure this is useless
		super.render(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void init(Minecraft minecraft, int width, int height){
		this.setSize(width, height);
		this.map.setSize(this.width, this.height);
		//this.map.setGuiSize(250, 200); //TODO
		//this.map.initGui();
		//if(this.map.zoomLevel != 0) {
			this.map.setZoomToMinimum();
		//} //TODO TEMPS DEBUG
		this.addButton(new Button(0, 0, 72, 20, "Quit", (p) ->  {this.minecraft.displayGuiScreen(this.parent);})); //TODO Localization and screen position, this is very much temporary
	}
	
}
