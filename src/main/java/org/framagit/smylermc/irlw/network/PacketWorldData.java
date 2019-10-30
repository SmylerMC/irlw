package org.framagit.smylermc.irlw.network;

import java.util.function.Supplier;

import org.framagit.smylermc.irlw.IRLW;
import org.framagit.smylermc.irlw.world.gen.IRLWGenerationSettings;
import org.framagit.smylermc.irlw.world.gen.IRLWGenerationSettings.InvalidSettingsException;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PacketWorldData {

	private IRLWGenerationSettings genSettings;
	private boolean reduceDebugInfo;
	
	public PacketWorldData(IRLWGenerationSettings genSettings, boolean reduceDebugInfo) {
		this.genSettings = genSettings;
		this.reduceDebugInfo = reduceDebugInfo;
	}
	
	public static PacketWorldData decode(PacketBuffer buf) {
		IRLWGenerationSettings genSettings = new IRLWGenerationSettings();
		try {
			genSettings.setZoomLevel(buf.readInt());
			genSettings.setSpawnLat(buf.readDouble());
			genSettings.setSpawnLong(buf.readDouble());
		} catch (InvalidSettingsException e) {
			IRLW.logger.error("Decoded an invalid generation settings packet");
			IRLW.logger.catching(e);
			IRLW.logger.error("Things may not be displayed correctly by client");
		}
		boolean reduceDebugInfo = buf.readBoolean();
		return new PacketWorldData(genSettings, reduceDebugInfo);
	}

	
	public static void encode(PacketWorldData packet, PacketBuffer buf) {
		// Only send data usefull for debuging
		buf.writeInt(packet.genSettings.getZoomLevel());
		buf.writeDouble(packet.genSettings.getSpawnLat());
		buf.writeDouble(packet.genSettings.getSpawnLong());
		buf.writeBoolean(packet.reduceDebugInfo);
	}
	
	public static void handle(PacketWorldData message, Supplier<Context> context) {
		IRLW.logger.debug("Recieved world data!");
		Minecraft.getInstance().deferTask(()->{
			World world = Minecraft.getInstance().world;
			world.getWorldInfo().setGeneratorOptions(message.genSettings.toNBT());
			world.getGameRules().get(GameRules.REDUCED_DEBUG_INFO).set(message.reduceDebugInfo, null);
			IRLW.logger.debug("Synchronized world data with server");
		});
		context.get().setPacketHandled(true);
	}
}
