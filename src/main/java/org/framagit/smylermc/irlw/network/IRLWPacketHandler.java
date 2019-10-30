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


package org.framagit.smylermc.irlw.network;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.framagit.smylermc.irlw.IRLW;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

/**
 * The default 
 * 
 * @author Smyler
 *
 */
public final class IRLWPacketHandler {

	//Protocol version information
	private static final String PROTOCOL_VERSION_MAJOR = "0"; // Needs to match
	private static final String PROTOCOL_VERSION_MINOR = "1";
	private static final String PROTOCOL_VERSION_EXTRA = "dev";
	public static final String PROTOCOL_VERSION = 
			IRLWPacketHandler.PROTOCOL_VERSION_MAJOR + "." +
			IRLWPacketHandler.PROTOCOL_VERSION_MINOR + "-" +
			IRLWPacketHandler.PROTOCOL_VERSION_EXTRA;
	
	
	
	//Packet discriminatory counter, should be increased for each packet type.
	private static int discriminator = 0;
	
	
	
	//The channel instance
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
				new ResourceLocation(IRLW.MOD_ID, "main"),
				() -> IRLWPacketHandler.PROTOCOL_VERSION,
				IRLWPacketHandler::isCompatible,
				IRLWPacketHandler::isCompatible
			);

	
	
	private static boolean isCompatible(String version) {
		//TODO Compare major only
		return IRLWPacketHandler.PROTOCOL_VERSION.equals(version);
	}
	
	
	
	/**
	 * Registers the handlers for the given side.
	 * 
	 * @param side
	 */
	public static void registerHandlers(){
		if(FMLEnvironment.dist.isClient()){
			registerClientHandlers();
		}
		registerServerHandlers();
	}
	
	
	
	/**
	 * Registers the server handlers
	 */
	private static void registerServerHandlers(){
		IRLW.logger.debug("Registering server network handlers");
	}
	
	
	
	/**
	 * Registers the client handlers
	 */
	private static void registerClientHandlers(){
		IRLW.logger.debug("Registering client network handlers");
		registerMessage(PacketWorldData.class, PacketWorldData::encode, PacketWorldData::decode, PacketWorldData::handle);
	}
	
	private static <MSG> void registerMessage(Class<MSG> type, BiConsumer<MSG, PacketBuffer> encoder, Function<PacketBuffer, MSG> decoder, BiConsumer<MSG, Supplier<Context>> consumer) {
        INSTANCE.registerMessage(discriminator++, type, encoder, decoder, consumer);
    }

	
}
