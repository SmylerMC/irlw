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
package com.github.smylermc.irlw.proxy;

import com.github.smylermc.irlw.IRLW;
import com.github.smylermc.irlw.network.IRLWPacketHandler;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraft.world.World;

/**
 * @author SmylerMC
 *
 */
public class IRLWServerProxy implements IRLWProxy{

	@Override
	public void preinit(FMLPreInitializationEvent event) {
		IRLWPacketHandler.registerHandlers(Side.SERVER);
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
		//TODO Implement this
	}

	@Override
	public void onInInvalidMapboxToken() {
		//TODO Implement this
		
	}

	@Override
	public void onGenerationError(World world, Exception e) {
		IRLW.logger.fatal("An exception occured when generating the world: " + e.toString());
		IRLW.logger.fatal("We have to stop the server!");
		world.getMinecraftServer().stopServer();
		//TODO Test this!
	}

	@Override
	public void stopServer() {
		// TODO Auto-generated method stub
		
	}

}
