package com.asangarin.breaker.network;

import com.asangarin.breaker.Breaker;
import com.asangarin.packkit.NetworkHandler;
import com.asangarin.packkit.PacketStatus;
import org.bukkit.entity.Player;

public class BreakerNetworkHandler implements NetworkHandler {
	@Override
	public PacketStatus readBefore(Player player, Object packet) {
		BlockDigPacketInfo info = Breaker.get().getPackkit().getNMS().readBlockDigPacket(packet);
		if(info == null) return PacketStatus.ALLOW;

		System.out.println("===================");
		System.out.println("Information Gotten:");
		System.out.println("Pos: " + info.getX() + ", " + info.getY() + ", " + info.getZ());
		System.out.println("DigType: " + info.getDigType());

		return PacketStatus.ALLOW;
	}

	@Override
	public PacketStatus writeBefore(Player player, Object packet) {
		return PacketStatus.ALLOW;
	}
}
