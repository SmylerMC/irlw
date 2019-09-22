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
package org.framagit.smylermc.irlw.client.gui.widget;

import org.framagit.smylermc.irlw.IRLW;
import org.framagit.smylermc.irlw.maps.utils.WebMercatorUtils;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

/**
 * @author SmylerMC
 *
 * A simple squared map, which can display a pointer
 *
 */
public class SimpleMapWidget extends Widget {

	private int size, px = 0, py = 0, pcolor;
	public boolean showPointer = false;
	
	public SimpleMapWidget(int x, int y, int size) {
		super(x, y, size, size, ""); //FIXME 1.14.4 - message text Localization for SimpleMapWidget constructor
		this.pcolor = 0xFF0000;
		this.size = size;
	}
	
	@Override
	public void renderButton(int mouseX, int mouseY, float partialTricks) {
		
		if(!this.visible) return;
		
		Widget.fill(this.x - 1, this.y - 1, this.x + this.size + 1, this.y + this.size + 1, 0xFF000000);
		GlStateManager.color4f(255, 255, 255, 255);
		TextureManager m = Minecraft.getInstance().getTextureManager();
		m.bindTexture(new ResourceLocation(IRLW.MOD_ID, "textures/gui/worldmap.png"));
		Widget.blit(this.x, this.y, 0, 0, this.size, this.size, this.size, this.size);
		if(this.showPointer) {
			Widget.fill(this.x + this.px - 2, this.y + this.py - 2, this.x + this.px + 1, this.y + this.py + 1, this.pcolor);
		}
	}
	
	public void setPointer(int x, int y, int color) {
		this.px = x;
		this.py = y;
		this.pcolor = color;
	}
	
	public void setPointerLongLat(double lon, double lat, int color) {
		int px = (int) (WebMercatorUtils.getXFromLongitude(lon, 0)/255*this.size);
    	int py = (int) (WebMercatorUtils.getYFromLatitude(lat, 0)/255*this.size);
    	this.setPointer(px, py, color);
	}
	
	public void setSize(int size){
		float k = (float)size/this.size;
		this.px = (int) (k * this.px);
		this.py = (int) (k* this.py);
		this.size = size;
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	
}
