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


package org.framagit.smylermc.irlw.client.events;

import org.framagit.smylermc.irlw.IRLW;
import org.framagit.smylermc.irlw.IRLWConfiguration;
import org.framagit.smylermc.irlw.client.gui.DebugScreenHandler;
import org.framagit.smylermc.irlw.client.gui.GuiInvalidTokenPrompt;
import org.framagit.smylermc.irlw.maps.utils.MapboxUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


/**
 * The event subscriber for generic client events such as networking
 * 
 * @author Smyler
 *
 */
@Mod.EventBusSubscriber(modid=IRLW.MOD_ID)
public class IRLWClientEventHandler {

	
	private static final DebugScreenHandler DEBUG = new DebugScreenHandler();
	
	
	/**
	 * Called by forge when rendering the debug screen.
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public static void onRenderGameOverlayText(RenderGameOverlayEvent.Text event){
		if(Minecraft.getMinecraft().gameSettings.showDebugInfo){
			event.getLeft().addAll(DEBUG.getLeft());
			event.getRight().addAll(DEBUG.getRight());
		}
	}
	
	
	@SubscribeEvent
	public static void onGuiPostInit(GuiScreenEvent.InitGuiEvent.Post event) {
		if(		!IRLWConfiguration.ignoreInvalidTokens
				&& event.getGui() instanceof GuiWorldSelection
				&& !MapboxUtils.checkToken(IRLWConfiguration.mapboxToken)) {
			IRLW.logger.info("Token is invalid, prompting for a valid token.");
			Minecraft.getMinecraft().displayGuiScreen(new GuiInvalidTokenPrompt(event.getGui()));
		}
	}
}
