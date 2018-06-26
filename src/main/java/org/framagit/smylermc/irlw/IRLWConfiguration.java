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

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author SmylerMC
 * 
 * IRLW's config
 *
 */
@Config(modid = IRLW.MOD_ID)
public class IRLWConfiguration{
	
	@Config.Comment("The mods needs access to Mapbox's online API to work, you need to create an account at mapbox.com")
	public static String mapboxToken = "";
	
	@Config.Comment("Should we ask if you want to set a mapbox token.")
	public static boolean ignoreInvalidTokens = false;
	
	@Config.Comment("Where to cache our files")
	public static String cachingDir = "IRLW_cache";

	
	@Config.Comment("The default zoom level to use when generating a world")
	public static int defaultZoomLevel = 0;
	
	public static void sync() {
		ConfigManager.sync(IRLW.MOD_ID, Config.Type.INSTANCE);	
	}
	
	@Mod.EventBusSubscriber(modid = IRLW.MOD_ID)
	private static class EventHandler {

		/**
		 * Inject the new values and save to the config file when the config has been changed from the GUI.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(IRLW.MOD_ID)) {
				IRLWConfiguration.sync();
			}
		}
}

}
