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

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

/**
 * @author SmylerMC
 *
 */
public class IRLWClientProxy implements IRLWProxy {

	@Override
	public void preinit(FMLPreInitializationEvent event) {

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
		
	}

	@Override
	public void onGenerationError(World world, Exception e) {
		//TODO improve
		IRLW.logger.error("An exception occured while generating, stoping the integrated server");
		Minecraft.getMinecraft().getIntegratedServer().stopServer();
	}

}
