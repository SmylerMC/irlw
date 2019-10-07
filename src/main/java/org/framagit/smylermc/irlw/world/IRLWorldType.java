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
package org.framagit.smylermc.irlw.world;

import org.framagit.smylermc.irlw.IRLW;
import org.framagit.smylermc.irlw.client.gui.CustomizeIRLWorldScreen;
import org.framagit.smylermc.irlw.world.gen.IRLWChunkGenerator;
import org.framagit.smylermc.irlw.world.gen.IRLWGenerationSettings;
import org.framagit.smylermc.irlw.world.gen.IRLWGenerationSettings.InvalidSettingsException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.CreateWorldScreen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.provider.OverworldBiomeProvider;
import net.minecraft.world.biome.provider.OverworldBiomeProviderSettings;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.storage.WorldInfo;

/**
 * @author SmylerMC
 *
 */
//FIXME 1.14.4 - Update world type to 1.14.4
public class IRLWorldType extends WorldType {

	/**
	 * @param name The name of the world type
	 */
	public IRLWorldType(String name) {
		super(name);
		this.setCustomOptions(true);
	}
	
	
    /**
     * Gets whether this WorldType can be used to generate a new world.
     */
	@Override
    public boolean canBeCreated(){
        return true; //TODO Check for network
    }
	

//    /**
//     * Gets the spawn fuzz for players who join the world.
//     * Useful for void world types.
//     * @return Fuzz for entity initial spawn in blocks.
//     */
//	@Override
//    public int getSpawnFuzz(ServerWorld world, net.minecraft.server.MinecraftServer server){
////        return super.getSpawnFuzz(world, server);
//        return 0;
//    }
    
    
	@Override
	public ChunkGenerator<IRLWGenerationSettings> createChunkGenerator(World world){
//    	return new IRLWTestChunkGenerator(world, null, null); //TODO TEMP
		WorldInfo worldInfo = world.getWorldInfo();
		CompoundNBT nbt = worldInfo.getGeneratorOptions();
		IRLWGenerationSettings settings;
		try {
			settings = new IRLWGenerationSettings(nbt);
		} catch (InvalidSettingsException e) {
			IRLW.logger.info("Could not read generation settings NBT, falling back to default");
			settings = new IRLWGenerationSettings();
		}
		OverworldBiomeProviderSettings biomeSettings = new OverworldBiomeProviderSettings();
		biomeSettings.setWorldInfo(worldInfo);
		return new IRLWChunkGenerator(world, new OverworldBiomeProvider(biomeSettings), settings);

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
    public void onCustomizeButton(Minecraft mc, CreateWorldScreen guiCreateWorld){
            mc.displayGuiScreen(new CustomizeIRLWorldScreen(mc, guiCreateWorld));
    }
}
