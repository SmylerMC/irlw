package org.framagit.smylermc.irlw.network;

import java.util.function.Supplier;

import org.framagit.smylermc.irlw.IRLW;
import org.framagit.smylermc.irlw.world.IRLWWorldData;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PacketWorldData {

	private IRLWWorldData data;
	
	public PacketWorldData(IRLWWorldData data) {
		this.data = data;
	}
	public static PacketWorldData decode(PacketBuffer buf) {
		IRLWWorldData data = new IRLWWorldData();
		data.setZoomLevel(buf.readInt());
		data.setCenterLatitude(buf.readDouble());
		data.setCenterLongitude(buf.readDouble());
		data.setDeltaX(buf.readLong());
		data.setDeltaZ(buf.readLong());
		return new PacketWorldData(data);
	}

	
	public static void encode(PacketWorldData packet, PacketBuffer buf) {
		buf.writeInt(packet.data.getZoomLevel());
		buf.writeDouble(packet.data.getCenterLatitude());
		buf.writeDouble(packet.data.getCenterLongitude());
		buf.writeLong(packet.data.getDeltaX());
		buf.writeLong(packet.data.getDeltaZ());
	}
	
	public static void handle(PacketWorldData message, Supplier<Context> context) {
		IRLW.logger.debug("Recieved world data!");
		//TODO 
		//Minecraft.getInstance().addScheduledTask();
		//IRLWWorldData.setForWorld(world, data);
	}
}
