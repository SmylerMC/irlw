package org.framagit.smylermc.irlw.network.client;

import org.framagit.smylermc.irlw.world.IRLWWorldData;

import com.google.common.base.Supplier;

import net.minecraftforge.fml.network.NetworkEvent;

public class IRLWClientPacketHandler {

	public static void handleIRLWWorldData(IRLWWorldData data, Supplier<NetworkEvent.Context> ctx) {
		//TODO 
		//Minecraft.getInstance().addScheduledTask();
		//IRLWWorldData.setForWorld(world, data);
	}
}
