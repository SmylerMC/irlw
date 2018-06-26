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
package org.framagit.smylermc.irlw.proxy;

import org.framagit.smylermc.irlw.IRLW;
import org.framagit.smylermc.irlw.network.IRLWPacketHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author SmylerMC
 *
 */
public class IRLWClientProxy implements IRLWProxy {

	@Override
	public void preinit(FMLPreInitializationEvent event) {
		IRLWPacketHandler.registerHandlers(Side.CLIENT);
	}

	@Override
	public void init(FMLInitializationEvent event) {

	}

	@Override
	public void postinit(FMLPostInitializationEvent event) {

	}

	@Override
	public void serverStart(FMLServerStartingEvent event) {
		
	}

	@Override
	public void failedToCache() {
		IRLW.logger.error("There was an error when cashing a file");
		stopServer();
	}

	@Override
	public void onInInvalidMapboxToken() {
		IRLW.logger.error("The mapbox token is invalid, we are making sure no server is running!");
		stopServer();
	}

	@Override
	public void onGenerationError(World world, Exception e) {
		IRLW.logger.error("There was an error when generating, trying to stop the integrated server");
		stopServer();
	}

	@Override
	public void stopServer() {
		if(Minecraft.getMinecraft().getIntegratedServer().isServerRunning()) {
			IRLW.logger.error("Trying to stop the integrated server");
			Minecraft.getMinecraft().world.sendQuittingDisconnectingPacket();

			Minecraft.getMinecraft().addScheduledTask(new Runnable() {

				@Override
				public void run() {
					Minecraft.getMinecraft().loadWorld((WorldClient)null);
					Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu()); //TODO Error screen
				}
				
			});
            
//			Minecraft.getMinecraft().loadWorld(null);
		}else {
			IRLW.logger.info("The integrated server is not running, we won't stop it.");
		}
	}

}
