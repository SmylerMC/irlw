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
package org.framagit.smylermc.irlw;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framagit.smylermc.irlw.caching.CacheManager;
import org.framagit.smylermc.irlw.maps.tiles.tiles.MapboxElevationTile;
import org.framagit.smylermc.irlw.network.IRLWPacketHandler;
import org.framagit.smylermc.irlw.proxy.IRLWProxy;
import org.framagit.smylermc.irlw.world.IRLWorldType;

import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

/**
 * @author SmylerMC
 *
 * IRLW's main mod class
 */
@Mod(IRLW.MOD_ID)
public class IRLW {
	
	
	//FIXME 1.14.4 - might be obsolete
	/* Static fields describing the mod */
	public static final String MOD_ID = "irlw";
	public static final String MOD_NAME = "In Real Life World";
	public static final String MOD_VERSION = "0.0.7.0-alpha";
	public static final String PROTOCOL_VERSION = IRLWPacketHandler.PROTOCOL_VERSION;
	public static final String MC_VERSIONS = "1.14.4";
	public static final String AUTHOR_EMAIL = "smyler@mail.com";
	public static final String WORLD_TYPE_NAME = "irlworld";

	// Wikimedia's map requires you to use a user agent with such information
	public static final String HTTP_USER_AGENT = 
			"IRLW Minecraft mod: https://github.com/SmylerMC/irlw v" +
			IRLW.MOD_VERSION +
			" contact: " + IRLW.AUTHOR_EMAIL;

	
	//FIXME 1.14.4 - Get rid of proxies
	/* Proxy things */
	public static final String CLIENT_PROXY_CLASS = "org.framagit.smylermc.irlw.proxy.IRLWClientProxy";
	public static final String SERVER_PROXY_CLASS = "org.framagit.smylermc.irlw.proxy.IRLWServerProxy";
    //@SidedProxy(clientSide = IRLW.CLIENT_PROXY_CLASS, serverSide = IRLW.SERVER_PROXY_CLASS)
	public static IRLWProxy proxy;
    
    
    /* Config and logger */
    public static final Logger logger = LogManager.getLogger();
    public static CacheManager cacheManager;
    
    
	public IRLW() {
		
        IRLW.logger.info("Building Config...");
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, IRLWConfiguration.CLIENT_CONFIG);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, IRLWConfiguration.COMMON_CONFIG);
		
		// Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        
		IRLWConfiguration.loadConfig(IRLWConfiguration.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("irlw-common.toml"));
		IRLWConfiguration.loadConfig(IRLWConfiguration.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve("irlw-client.toml"));

		
		
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
	}
	
	
	private void setup(final FMLCommonSetupEvent event) {
		
		//FIXME 1.14.4 init
		
		IRLW.logger.debug("Starting IRLW setup...");
				
		try {
			IRLW.cacheManager = new CacheManager(IRLWConfiguration.cachingDir.get());
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
		//IRLW.proxy.preinit(event); //FIXME 1.14.4 - Proxy preinit replacement
		
		//IRLW.proxy.init(event); //FIXME 1.14.4 - Proxy init replacement
		
		IRLW.cacheManager.cacheAsync(new MapboxElevationTile(0, 0, 0));
		
		IRLW.logger.debug("IRLW setup is done");
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
    	/*
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
        */
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        //InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event) {
    	/*
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
        */
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
		IRLW.logger.debug("IRLW serverStart...");
		//IRLW.proxy.serverStart(event); //FIXME 1.14.4 - Proxy server start replacement
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (ts subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            //LOGGER.info("HELLO from Register Block");
        }
    }
	
}
