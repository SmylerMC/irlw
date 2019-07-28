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

import java.io.IOException;

import org.framagit.smylermc.irlw.maps.TileFactory;
import org.framagit.smylermc.irlw.maps.TiledMap;
import org.framagit.smylermc.irlw.maps.tiles.tiles.MapboxSatelliteTile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

/**
 * @author SmylerMC
 *
 * TODO Type comment
 *
 */
public class GuiScreenPickLocation extends GuiScreen {

	private GuiTiledMap map;
	private GuiScreen parent;
	
	private final int QUIT_ID = 0;
	
	public GuiScreenPickLocation(GuiScreen parent) {
		this.map = new GuiTiledMap(new TiledMap<MapboxSatelliteTile>(TileFactory.MAPBOX_SATELLITE_TILE_FACTORY));
		this.parent = parent;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.map.draw();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void initGui(){
		this.map.setGuiSize(this.width, this.height);
		//this.map.setGuiSize(250, 200); //TODO
		//this.map.initGui();
		//if(this.map.zoomLevel != 0) {
			this.map.setZoomToMinimum();
		//} //TODO TEMPS DEBUG
		this.addButton(new GuiButton(QUIT_ID , 0, 0, "Quit")); //TODO Localization
	}
	
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
	}

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
       super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /**
     * Called when a mouse button is released.
     */
    protected void mouseReleased(int mouseX, int mouseY, int state){
        super.mouseReleased(mouseX, mouseY, state);
    }

    /**
     * Called when a mouse button is pressed and the mouse is moved around. Parameters are : mouseX, mouseY,
     * lastButtonClicked & timeSinceMouseClick.
     */
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick){
    	super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException {
    	switch(button.id) {
    	case QUIT_ID:
    		Minecraft.getMinecraft().displayGuiScreen(this.parent);
    		break;
    	}
    	super.actionPerformed(button);
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput() throws IOException {
    	super.handleMouseInput();
    }

    /**
     * Handles keyboard input.
     */
    public void handleKeyboardInput() throws IOException {
    	super.handleKeyboardInput();
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {
    	this.map.updateScreen();
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed() {}
}
