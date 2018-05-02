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
package com.github.smylermc.irlw.maps.tiles;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import com.github.smylermc.irlw.IRLW;
import com.github.smylermc.irlw.caching.Cachable;
import com.github.smylermc.irlw.maps.exceptions.InvalidMapboxSessionException;
import com.github.smylermc.irlw.maps.utils.ImageUtils;
import com.github.smylermc.irlw.maps.utils.MapboxUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

/**
 * @author SmylerMC
 *
 */
public abstract class RasterWebTile implements Cachable {

	protected long x;
	protected long y;
	protected int zoom;
	protected int[] defaultPixel = {0, 0, 0, 0}; //What to return when the tile doesn't exist but should
	protected int size;
	protected BufferedImage image;
	protected ResourceLocation texture = null;
	

	public RasterWebTile(int size, long x, long y, int zoom) {

		this.x = x;
		this.y = y;
		this.zoom = zoom;
		this.size = size;

		if(!MapboxUtils.isTileInWorld(x, y, zoom))
			throw new InvalidTileCoordinatesException(this);

	}


	public RasterWebTile(int size, long x, long y, int zoom, int[] defaultPixel) {

		this(size, x, y, zoom);
		this.defaultPixel = defaultPixel;

	}


	@Override
	public abstract URL getURL();


	@Override
	public String getFileName(){
		return this.getClass().getName() + "_" + this.getZoom() + "_" + this.getX() + "_" + this.getY() + ".png";
	}


	public BufferedImage getImage() throws IOException, InvalidMapboxSessionException {
		if(this.image == null) {
			File f = IRLW.cacheManager.getFile(this);

			//If the file has been cached, it may have been loaded in this::cached
			if(this.image == null) this.loadImageFomFile(f);
		}
		return this.image;
	}


	@Override
	public void cached(File f) {

		try {
			this.loadImageFomFile(f);
		} catch (IOException e) {
			IRLW.logger.error("Got an IOException when reading a file which should have been properly cached!");
			IRLW.logger.catching(e);
		}
	}


	private void loadImageFomFile(File f) throws IOException {
		if(!f.exists() || !f.isFile()) {
			this.image = ImageUtils.imageFromColor(this.size, this.size, this.defaultPixel);
		} else {
			this.image = ImageIO.read(f);
		}
	}
	/**
	 * 
	 * @param x
	 * @param y
	 * @return The value of the pixel at x and y
	 * @throws InvalidMapboxSessionException 
	 * @throws IOException 
	 */
	public int[] getPixel(int x, int y) throws IOException, InvalidMapboxSessionException {
		return ImageUtils.decodeRGBA2Array(this.getImage().getRGB(x, y));
	}

	
	public ResourceLocation getTexture() {
		if(this.texture == null) {
			Minecraft mc = Minecraft.getMinecraft();
			TextureManager textureManager = mc.getTextureManager();
			try {
				BufferedImage image = this.getImage();
				this.texture = textureManager.getDynamicTextureLocation("textures/gui/maps/" + this.x + "/" + this.y + "/" + this.zoom, new DynamicTexture(image));
			} catch (IOException e) {
				IRLW.logger.catching(e);
			} catch (InvalidMapboxSessionException e) {
				IRLW.logger.catching(e);
			}
		}
		return this.texture;
		//TODO We should be able to unload textures
	}
	
	
	///// Various uninteresting getters and setters from here /////

	public long getX() {
		return x;
	}

	public long getY() {
		return y;
	}

	public int getZoom() {
		return zoom;
	}
	
	public long getXinPixel() {
		return this.x * this.size;
	}
	
	public long getYinPixel() {
		return this.y * this.size;
	}
	
	public int getSideSize() {
		return this.size;
	}


	public class InvalidTileCoordinatesException extends RuntimeException{

		/**
		 * @param string
		 */
		public InvalidTileCoordinatesException(RasterWebTile t) {
			super("Invalid tile coordinates: " + t.zoom + "/" + t.x + "/" + t.y);
		}

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		

	}
	
}
