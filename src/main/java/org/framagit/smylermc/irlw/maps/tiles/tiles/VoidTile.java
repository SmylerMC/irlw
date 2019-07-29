package org.framagit.smylermc.irlw.maps.tiles.tiles;

import java.net.URL;

import org.framagit.smylermc.irlw.IRLW;
import org.framagit.smylermc.irlw.maps.tiles.RasterWebTile;

import net.minecraft.util.ResourceLocation;

public class VoidTile extends RasterWebTile {

	public VoidTile() {
		super(256, 0, 0, 0);
	}

	@Override
	public ResourceLocation getTexture() {
		return new ResourceLocation(IRLW.MOD_ID, "textures/map/void.png");
	}
	
	@Override
	public URL getURL() {
		return null;
	}

}
