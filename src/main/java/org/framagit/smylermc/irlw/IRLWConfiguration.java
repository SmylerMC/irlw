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

import java.nio.file.Path;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

/**
 * @author SmylerMC
 * 
 * IRLW's config
 *
 */
@Mod.EventBusSubscriber
public class IRLWConfiguration{
	
	public static final String CATEGORY_GENERAL = "general";
	public static final String CATEGORY_MAPBOX = "mapbox";
	
	public static final ForgeConfigSpec COMMON_CONFIG;
	private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
	
	public static final ForgeConfigSpec CLIENT_CONFIG;
	private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

	
	public static ForgeConfigSpec.ConfigValue<String> mapboxToken;
	public static final String MAPBOX_TOKEN_COMMENT = "The mods needs access to Mapbox's online API to work, you need to create an account at mapbox.com";
	
	public static ForgeConfigSpec.BooleanValue ignoreInvalidTokens;
	public static final String IGNORE_TOKEN_COMMENT = "Should we ask if you want to set a mapbox token.";
		
	public static ForgeConfigSpec.ConfigValue<String> cachingDir;
	public static final String CACHE_DIR_COMMENT = "Where to cache our files";
	
	public static ForgeConfigSpec.IntValue defaultZoomLevel;
	public static final String DEFAULT_ZOOM_COMMENT = "The default zoom level to use when generating a world";
	
	static {
		COMMON_BUILDER.comment("General Settings").push(IRLWConfiguration.CATEGORY_GENERAL);
		cachingDir = COMMON_BUILDER.comment(CACHE_DIR_COMMENT).define("cache_directory", "cache");
		defaultZoomLevel = COMMON_BUILDER.comment(DEFAULT_ZOOM_COMMENT).defineInRange("default_zoom_level", 7, 0, 15);
		COMMON_BUILDER.pop();
		
		COMMON_BUILDER.comment("Mapbox Settings").push(CATEGORY_MAPBOX);
		mapboxToken = COMMON_BUILDER.comment(MAPBOX_TOKEN_COMMENT).define("mapbox_token", "");
		ignoreInvalidTokens = COMMON_BUILDER.comment(IGNORE_TOKEN_COMMENT).define("ignore_invalid_tokens", false);
		
		COMMON_BUILDER.pop();
		
		IRLW.logger.info("Building configs");
		COMMON_CONFIG = COMMON_BUILDER.build();
		CLIENT_CONFIG = CLIENT_BUILDER.build();
	}

	public static void loadConfig(ForgeConfigSpec spec, Path path) {
		
		IRLW.logger.info("Loading config");
		
		final CommentedFileConfig configData = CommentedFileConfig.builder(path)
				.sync()
				.autosave()
				.writingMode(WritingMode.REPLACE)
				.build();
		configData.load();
		spec.setConfig(configData);
	}

	@SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {

    }

    @SubscribeEvent
    public static void onReload(final ModConfig.ConfigReloading configEvent) {
    	
    }
    
}
