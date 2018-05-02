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
package com.github.smylermc.irlw.world;

import com.github.smylermc.irlw.IRLW;
import com.github.smylermc.irlw.client.gui.GuiCustomizeIRLWorld;
import com.github.smylermc.irlw.world.gen.IRLWChunkGenerator;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.IChunkGenerator;

/**
 * @author SmylerMC
 *
 */
public class IRLWorldType extends WorldType {

	/**
	 * @param name The name of the world type
	 */
	public IRLWorldType(String name) {
		super(name);
	}
	
	
    /**
     * Gets whether this WorldType can be used to generate a new world.
     */
	@Override
    public boolean canBeCreated(){
        return true; //TODO Check for network
    }
	
	
    /**
     * Gets the spawn fuzz for players who join the world.
     * Useful for void world types.
     * @return Fuzz for entity initial spawn in blocks.
     */
    public int getSpawnFuzz(WorldServer world, net.minecraft.server.MinecraftServer server){
//        return super.getSpawnFuzz(world, server);
        return 0;
    }
    
    
    public IChunkGenerator getChunkGenerator(World world, String generatorOptions){
    	JsonParser parser = new JsonParser();
		try{
    		JsonObject jsonObject = parser.parse(generatorOptions).getAsJsonObject();
    		int zoomLevel = jsonObject.get("zoomlevel").getAsInt();
    		double spawnLong = jsonObject.get("spawn_long").getAsDouble();
    		double spawnLat = jsonObject.get("spawn_lat").getAsDouble();
    		 return new IRLWChunkGenerator(world, world.getSeed(), spawnLong, spawnLat, zoomLevel);
    	}catch(Exception e){
    		IRLW.logger.error("Failed to create a chunk generator: ");
    		IRLW.logger.catching(e);
    		return new IRLWChunkGenerator(world, world.getSeed(), 0, 0, 0);
    	}
    }
    
	/**
     * Called when 'Create New World' button is pressed before starting game
     */
	@Override
    public void onGUICreateWorldPress() {
    	IRLW.logger.info("Creating a new IRLW world");
    }
    
	
	/**
	 * Make the Customize button visible
	 */
	public boolean isCustomizable(){
		return true;
	}
	
	
    /**
     * Called when the 'Customize' button is pressed on world creation GUI
     * @param mc The Minecraft instance
     * @param guiCreateWorld the createworld GUI
     */
    @Override
    public void onCustomizeButton(Minecraft mc, GuiCreateWorld guiCreateWorld){
            mc.displayGuiScreen(new GuiCustomizeIRLWorld(mc, guiCreateWorld));
    }
}
