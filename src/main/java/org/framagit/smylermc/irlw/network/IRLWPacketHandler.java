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

import org.framagit.smylermc.irlw.IRLW;
import org.framagit.smylermc.irlw.world.IRLWWorldData;

import net.minecraftforge.fml.network.NetworkRegistry;

/**
 * The default 
 * 
 * @author Smyler
 *
 */
public final class IRLWPacketHandler {

	//The channel instance
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(IRLW.MOD_ID);
	
	//Packet discriminator counter, should be increased for each packet type.
	private static int discriminator = 0;
	
	
	/**
	 * Registers the handlers for the given side.
	 * 
	 * @param side
	 */
	public static void registerHandlers(Side side){
		if(side.isClient()){
			registerClientHandlers();
		}
			registerServerHandlers();
	}
	
	
	/**
	 * Registers the server handlers
	 */
	private static void registerServerHandlers(){
		IRLW.logger.debug("Registenring server network handlers");
	}
	
	/**
	 * Registers the client handlers
	 */
	private static void registerClientHandlers(){
		IRLW.logger.debug("Registenring client network handlers");
		INSTANCE.registerMessage(IRLWWorldData.IRLWWorldDataMessageHandler.class, IRLWWorldData.class, discriminator++, Side.CLIENT);
	}
	
}
