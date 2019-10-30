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
import org.framagit.smylermc.irlw.network.PacketWorldData;
import org.framagit.smylermc.irlw.world.gen.IRLWGenerationSettings;
import org.framagit.smylermc.irlw.world.gen.IRLWGenerationSettings.InvalidSettingsException;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;

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
	public static void onPlayerJoinServer(PlayerLoggedInEvent event) {
		//TODO Resync when gamerule reducedDebugInfo changes
		ServerPlayerEntity player = (ServerPlayerEntity)event.getPlayer();
		syncDebugInfoIfNeeded(player);
	}
	
//	@SubscribeEvent
//	public static void onCommand(CommandEvent event) {
//		@SuppressWarnings("rawtypes")
//		CommandContextBuilder context = event.getParseResults().getContext();
//		if(context.getCommand().toString().startsWith("net.minecraft.command.impl.GameRuleCommand")) {
//			IRLW.logger.info(context.getSource());
//			if(context.getSource() instanceof CommandSource) {
//				CommandSource src = (CommandSource)context.getSource();
//				try {
//					ServerPlayerEntity player = src.asPlayer();
//					syncDebugInfoIfNeeded(player); //FIXME This is done to soon: the gamerule hasn't changed yet at this point
//				} catch (CommandSyntaxException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
	
	public static void syncDebugInfoIfNeeded(ServerPlayerEntity player) {
		World world = player.getEntityWorld();
		
		if(world.getWorldType().getName().equals(IRLW.WORLD_TYPE_NAME)){
			IRLW.logger.debug("Sending debug data to client " + player.getName().getString());
			boolean reducedDebugInfo = world.getGameRules().getBoolean(GameRules.REDUCED_DEBUG_INFO);
			try {
				IRLWGenerationSettings settings;
				if(!reducedDebugInfo)
					settings = new IRLWGenerationSettings(world.getWorldInfo().getGeneratorOptions());
				else
					settings = new IRLWGenerationSettings(); // Do not send settings if gamerule reduced_debug_info is false
				PacketWorldData packet = new PacketWorldData(settings, reducedDebugInfo);
				IRLWPacketHandler.INSTANCE.sendTo(packet, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
			} catch (InvalidSettingsException e) {
				IRLW.logger.error("The current world seems to have invalid generation settings");
				IRLW.logger.catching(e);
				IRLW.logger.error("World data were not sent to the client");
			}
			
		}
	}
	
}
