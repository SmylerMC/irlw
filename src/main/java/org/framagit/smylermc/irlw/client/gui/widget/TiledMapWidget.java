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
package org.framagit.smylermc.irlw.client.gui.widget;

import org.framagit.smylermc.irlw.IRLW;
import org.framagit.smylermc.irlw.maps.TiledMap;
import org.framagit.smylermc.irlw.maps.tiles.RasterWebTile;
import org.framagit.smylermc.irlw.maps.tiles.RasterWebTile.InvalidTileCoordinatesException;
import org.framagit.smylermc.irlw.maps.tiles.tiles.VoidTile;
import org.framagit.smylermc.irlw.maps.utils.IRLWUtils;
import org.framagit.smylermc.irlw.maps.utils.WebMercatorUtils;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.texture.TextureManager;

/**
 * @author Smyler
 *
 */
public class TiledMapWidget extends Widget{
	
	//A multiplier to use when scaling the textures
	private final float GUI_SIZING = 3; //TODO
	
	//The map we are rendering
	protected TiledMap<?> map;
	
	//The coordinates of the center of the map
	protected double focusLatitude; 
	protected double focusLongitude;
	
	//Whether to show tiles borders or not
	protected boolean debug = false;
	
	protected double zoomLevel;
	protected double zoomMotion = 0;
	
	
	public TiledMapWidget(int x, int y, int width, int height, String message, TiledMap<?> map) {
		super(x, y, width, height, message);
		this.map = map;
		this.zoomLevel = map.getZoomLevel();
		this.focusLatitude = 0;
		this.focusLongitude = 0;
	}
    
	//FIXME Zooming to much kills it
	@Override
	public void renderButton(int mouseX, int mouseY, float partialTricks) {
		
		//Input Handling
		//FIXME 1.14.4 - handle keyboard input in a method
//		if(Keyboard.isKeyDown(Keyboard.KEY_L)) {
//			this.debug = !this.debug;
//		}
		
		//TODO What is this for? Spamming the logs?
		if((int)this.zoomLevel != this.map.getZoomLevel()) {
			IRLW.logger.info("Zooms are differents: GUI: " + this.zoomLevel + " | Map: " + this.map.getZoomLevel());
		}
		
		//Compute various values used when rendering
		double renderFactor = this.getSizeFactor();
		int renderSize = (int) (renderFactor * WebMercatorUtils.TILE_DIMENSIONS);
		long upperLeftX = (long)(
				(double)(WebMercatorUtils.getXFromLongitude(this.focusLongitude, (int)this.zoomLevel))
				* renderFactor
				- ((double)this.width) / 2f);
		long upperLeftY = (long)(
				(double)WebMercatorUtils.getYFromLatitude(this.focusLatitude, (int)this.zoomLevel)
				* renderFactor
				- (double)this.height / 2f);
		int maxTileXY = (int) map.getSizeInTiles();
		long maxX = upperLeftX + this.width;
		long maxY = upperLeftY + this.height;
		int lowerTX = IRLWUtils.roundSmaller((double)upperLeftX / (double)renderSize);
		int lowerTY = IRLWUtils.roundSmaller((double)upperLeftY / (double)renderSize);
		
		//Get ready for main rendering loop
		Minecraft mc = Minecraft.getInstance();
		TextureManager textureManager = mc.getTextureManager();

		//For each tile spot on the screen:
		for(int tX = lowerTX; tX * renderSize < maxX; tX++) {
			for(int tY = lowerTY; tY * renderSize < maxY; tY++) {

				//Select the tile object when are trying to render
				RasterWebTile tile = new VoidTile();
				try {
					tile = map.getTile(IRLWUtils.modulus(tX, maxTileXY), tY, (int) this.zoomLevel);
				}catch(InvalidTileCoordinatesException e) {
				}
				boolean lowerResRender = !IRLW.cacheManager.isCached(tile); //Has it been cached?
				RasterWebTile bestTile = tile; //Keep a copy of it even when rendering lower res tiles
				
				//Tell the CacheManager to do its job if needed
				if(lowerResRender) {
					if(!IRLW.cacheManager.isBeingCached(tile))
						IRLW.cacheManager.cacheAsync(tile);
					while(tile.getZoom() > 0 && !IRLW.cacheManager.isCached(tile)) {
						tile = this.map.getTile(tile.getX() /2, tile.getY() /2, tile.getZoom()-1);
					}
				}
				
				//Compute where the tile go on the screen
				int dispX = Math.round(this.x + tX * renderSize - upperLeftX);
				int displayWidth = (int) Math.min(renderSize, maxX - tX * renderSize);
				int dispY = Math.round(this.y + tY * renderSize - upperLeftY);
				int displayHeight = (int) Math.min(renderSize, maxY - tY * renderSize);
				
				//Compute the part of the tile's texture to render
				int renderSizedSize = renderSize;
				int dX = 0;
				int dY = 0;
				if(lowerResRender) {
					int sizeFactor = (1 <<(bestTile.getZoom() - tile.getZoom()));
					
					int xInBiggerTile = (int) (bestTile.getX() - sizeFactor * tile.getX());
					int yInBiggerTile = (int) (bestTile.getY() - sizeFactor * tile.getY());
					
					double factorX = (double)xInBiggerTile / (double)sizeFactor;
					double factorY = (double)yInBiggerTile / (double)sizeFactor;
					renderSizedSize *= sizeFactor;
					dX += (int) (factorX * renderSizedSize);
					dY += (int) (factorY * renderSizedSize);
				}
				if(tX == lowerTX) { //Remove the left side of the tiles on the left
					dX += this.x-dispX;
					dispX = this.x;
				}
				if(tY == lowerTY) { //Remove the upper side of the tiles on the top
					dY += this.y-dispY;
					dispY = this.y;
				}
				
				//TODO Remove the right and bottom tiles

				//Render the tile
				textureManager.bindTexture(tile.getTexture());
				Widget.blit(
						dispX,
						dispY,
						dX, dY,
						displayWidth,
						displayHeight,
						renderSizedSize,
						renderSizedSize);
				
				//Draw additional debug information
				if(this.debug) {
					final int RED = 0xFFFF0000;
					final int WHITE = 0xFFFFFFFF;
					this.hLine(
							dispX,
							dispX + displayWidth - 1,
							dispY,
							lowerResRender? RED : WHITE);
					this.hLine(
							dispX,
							dispX + displayWidth - 1,
							dispY + displayHeight - 1,
							lowerResRender? RED : WHITE);
					this.vLine(
							dispX,
							dispY,
							dispY + displayHeight - 1,
							lowerResRender? RED : WHITE);
					this.vLine(
							dispX + displayWidth - 1,
							dispY,
							dispY + displayHeight - 1,
							lowerResRender? RED : WHITE);
				}
				GlStateManager.color4f(255, 255, 255, 255);

				
			}
			
		}
		
	}
    
    
	
    /**
     * Called when a mouse button is pressed and the mouse is moved around. Parameters are : mouseX, mouseY,
     * lastButtonClicked & timeSinceMouseClick.
     */
	@Override
    protected void onDrag(double p1, double p2, double p3, double p4){ //FIXME 1.14.4 - Make sure this parameters are what they seem to be
    	IRLW.logger.info("click move");  //TODO Remove useless log
    	super.onDrag(p1, p2, p3, p4);
    }
				
	public float getMouseLongitude() {
		//TODO
		return 0f;
	}
	
	public float getMouseLatitude() {
		//TODO
		return 0f;
	}
	
//FIXME 1.14.4 - Use the provided widget methods to handle keyboard and mouse input in TiledMap	
//    /**
//     * Handles mouse input.
//     */
//    public void handleMouseInput(){
//    	
//    	//TODO Make sure the mouse is within the map first
//    	
//    	//Moving
//    	if(Mouse.isButtonDown(0)) {
//    		
//    		int dX = Mouse.getDX();
//    		int dY = Mouse.getDY();
//    		
//    		//TODO Take the mercator projection into account
//    		this.setPosition(
//    				this.focusLongitude - dX/Math.pow(2, this.zoomLevel) * this.GUI_SIZING,
//    				this.focusLatitude - dY/Math.pow(2, this.zoomLevel) * this.GUI_SIZING);
//    	}
//    	
//    	//Scrolling
//        int i = Mouse.getDWheel();
//        int z;
//        if (i != 0){
//        	
//            if (i > 0) z = 1;
//            else z = -1;
//            
//            //TODO remove zoom motion
//            
//            this.zoom(z);
//        }
//    }
//    
    public void zoom(double val) {
    	
    	double nzoom = this.zoomLevel + val;
    	//int newRenderSize = this.getTileRenderSize(nzoom, this.map.getZoomLevel());
    	
    	if(nzoom < 0 || this.getMaxMapSize((int)nzoom) < this.height) {
    		return;
    	}
    	
    	//int oldRenderSize = this.getTileRenderSize(this.map.getZoomLevel());
    	
    	//int mouseX = Mouse.getX() - this.x;
    	//int mouseY = Mouse.getY() - this.y;
    	
    	//TODO TEMP
    	//IRLW.logger.info(mouseX);
    	//IRLW.logger.info(mouseY);
//    	long newUpperLeftX = (long) ((double)this.upperLeftX / oldRenderSize * newRenderSize);
    	//long newUpperLeftY = (long) ((double)this.upperLeftY / oldRenderSize * newRenderSize);
    	//long newUpperLeftX = (long) ((double)this.upperLeftX / oldRenderSize * newRenderSize);
    	//newUpperLeftX -=  (double)mouseX/this.width/oldRenderSize * newRenderSize;
//    	long newUpperLeftY = (long) ((double)(this.upperLeftY - mouseY) / oldRenderSize * newRenderSize);
//    	IRLW.logger.info(newUpperLeftX);
//    	IRLW.logger.info(newUpperLeftY);
    	//if(this.setPosition(newUpperLeftX, newUpperLeftY)){    	
    		this.zoomLevel = nzoom;
    	//}
    	this.setTiledMapZoom();
    	
    	//FIXME tiled map
    }
    

    /**
     * Set the zoom to the minimum zoom possible while making sure the map is still scaled so it fills the screen
     */
    public void setZoomToMinimum() {
    	this.focusLongitude = 0;
    	this.focusLatitude = 0;
    	this.setZoom(0);
    	this.setZoom(Math.max(0, (float) (Math.log(this.height * this.GUI_SIZING / WebMercatorUtils.TILE_DIMENSIONS) / Math.log(2))));
    }
    
    private void setTiledMapZoom() {
    	this.map.setZoomLevel((int)this.zoomLevel);
    }
    
    //TODO this is a mess
    private double getSizeFactor() {
    	return 1 / this.GUI_SIZING;
    }
    
    private long getMaxMapSize(int zoom) {
    	//TODO May overflow ?
    	return (long) (WebMercatorUtils.getDimensionsInTile(zoom) * this.getSizeFactor() * WebMercatorUtils.TILE_DIMENSIONS); //TODO Adapt according to Minecraft's GUI sizing setting
    }
    
    /* === Getters and Setters from this point === */

    public void setSize(int width, int height) {
    	this.width = width;
    	this.height = height;
    }
    
    public boolean setPosition(double lon, double lat) {
    	
    	return this.setLongitude(lon) && this.setLatitude(lat);
    	
    	//TODO remake with new system
    	/*
		if(y >= 0 && y + this.height < this.getMaxMapSize(this.map.getZoomLevel())) {
			this.upperLeftX = x % this.map.getSizeInPixels();
			this.upperLeftY = y;
			return true;
		}
		return false;
		*/
    	
    }
    
    public boolean setLongitude(double lon) {
    	this.focusLongitude = lon;
    	return true;
    	//TODO Check if the new position is legal
    }
	
    public boolean setLatitude(double lat) {
    	this.focusLatitude = lat;
    	return true;
    	//TODO Check if the new position is legal
    }
    
    private void setZoom(float zoom) {
    	this.zoomLevel = zoom;
    	this.setTiledMapZoom();
    }
}
