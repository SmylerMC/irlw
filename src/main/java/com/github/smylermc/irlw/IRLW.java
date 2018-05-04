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
package com.github.smylermc.irlw;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.Logger;

import com.github.smylermc.irlw.caching.CacheManager;
import com.github.smylermc.irlw.maps.tiles.tiles.MapboxElevationTile;
import com.github.smylermc.irlw.proxy.IRLWProxy;
import com.github.smylermc.irlw.world.IRLWorldType;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

/**
 * @author SmylerMC
 *
 * IRLW's main mod class
 */
@Mod(modid = IRLW.MOD_ID,
version = IRLW.MOD_VERSION,
acceptedMinecraftVersions = IRLW.MC_VERSIONS,
name = IRLW.MOD_NAME)
public class IRLW {
	
	
	/* Static fields describing the mod */
	public static final String MOD_ID = "irlw";
	public static final String MOD_NAME = "In Real Life World";
	public static final String MOD_VERSION = "0.0.3.1-alpha";
	public static final String MC_VERSIONS = "1.12.2";
	public static final String AUTHOR_EMAIL = "smyler@mail.com";
	public static final String WORLD_TYPE_NAME = "irlworld";
	
	public static final String HTTP_USER_AGENT = 
			"IRLW Minecraft mod: https://github.com/SmylerMC/irlw. v" +
			IRLW.MOD_VERSION +
			" contact: " + IRLW.AUTHOR_EMAIL;

	
	/* Proxy things */
	public static final String CLIENT_PROXY_CLASS = "com.github.smylermc.irlw.proxy.IRLWClientProxy";
	public static final String SERVER_PROXY_CLASS = "com.github.smylermc.irlw.proxy.IRLWServerProxy";
    @SidedProxy(clientSide = IRLW.CLIENT_PROXY_CLASS, serverSide = IRLW.SERVER_PROXY_CLASS)
	public static IRLWProxy proxy;
    
    
    /* Config and logger */
    public static Logger logger;
    public static File configDir;
    public static CacheManager cacheManager;
    
    
	
    /**
     * Called by fml while loading the mod. What should be done here:
     * 
     * 	-Registering blocks and items should be done sooner in the respective classes
     * 	-Register tile entities
     * 	-Register entities
     * 	-Assign ore dictionary name
     *  -Register packet handlers?
     *  
     * @param event
     */
	@EventHandler
	public void preinit(FMLPreInitializationEvent event) {
		IRLW.logger = event.getModLog();
		IRLW.logger.debug("IRLW preinit...");
		
		IRLW.configDir = event.getModConfigurationDirectory();
		
		try {
			IRLW.cacheManager = new CacheManager(IRLWConfiguration.cachingDir);
			IRLW.cacheManager.createDirectory();
		} catch (IOException e) {
			IRLW.logger.catching(e);
			IRLW.logger.error("Caching directory doesn't seem to be valid, we will use a temporary one.");
			IRLW.logger.error("Make sure your config is correct!");
			IRLW.cacheManager = new CacheManager();
			
		}
		IRLW.cacheManager.startWorker();
		
		IRLW.logger.info("Registering world type: " + IRLW.WORLD_TYPE_NAME);
		new IRLWorldType(IRLW.WORLD_TYPE_NAME); //Instantiating is enough
		IRLW.proxy.preinit(event);
	}
	
	
	/**
     * Called by fml while loading the mod. What should be done here:
     * 
     * 	-Register world generators
     * 	-Register recipes
     * 	-Register event handlers
     * 	-Send IMC messages
     *  
     * @param event
     */
	@EventHandler
	public void init(FMLInitializationEvent event) {
		IRLW.logger.debug("IRLW init...");
		IRLW.proxy.init(event);
	}
	
	
	/**
     * Called by fml while loading the mod. What should be done here:
     * 
     * 	-Mod compatibility stuff
     *  
     * @param event
     */
	@EventHandler
	public void postinit(FMLPostInitializationEvent event) {
		IRLW.logger.debug("IRLW postinit...");
		
		IRLW.cacheManager.cacheAsync(new MapboxElevationTile(0, 0, 0));
		
		IRLW.proxy.postinit(event);
		
	}
	
	
	/**
     * Called by fml while starting a server, BUT AFTER THE WORLD HAS BEEN GENERATED!
     * Commands should be registered here.
     * 
     * @param event
     */
	@EventHandler
	public void serverStart(FMLServerStartingEvent event) {
		IRLW.logger.debug("IRLW serverStart...");
		IRLW.proxy.serverStart(event);
		
	}
	
}