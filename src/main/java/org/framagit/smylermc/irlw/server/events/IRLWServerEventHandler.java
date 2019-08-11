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


package org.framagit.smylermc.irlw.server.events;

import org.framagit.smylermc.irlw.IRLW;
import org.framagit.smylermc.irlw.network.IRLWPacketHandler;
import org.framagit.smylermc.irlw.world.IRLWWorldData;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * The event subscriber for generic server events
 * 
 * @author Smyler
 *
 */
@Mod.EventBusSubscriber(modid=IRLW.MOD_ID)
public final class IRLWServerEventHandler {

	/**
	 * Fired on server side when a player is about to join.
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public static void onPlayerJoinServer(PlayerLoggedInEvent event){
		//Send world data to the client
		PlayerEntity player = (PlayerEntity)event.getPlayer();
		World world = player.getEntityWorld();
		if(world.getWorldType().getName().equals(IRLW.WORLD_TYPE_NAME)){
			IMessage data = (IMessage) world.loadData(IRLWWorldData.class, IRLWWorldData.IRLW_DATA);
			if(data == null) {
				IRLWWorldData.setForWorld(world);
			}
			data = (IMessage) world.loadData(IRLWWorldData.class, IRLWWorldData.IRLW_DATA);
			if(data == null) {
				IRLW.logger.fatal("Failed to load some data we just set before sending it, we are going to crash!!");
			}
			IRLWPacketHandler.INSTANCE.sendTo(data, player);
		}
	}
	
}
