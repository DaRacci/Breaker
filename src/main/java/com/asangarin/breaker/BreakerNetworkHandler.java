package com.asangarin.breaker;

import com.asangarin.packkit.NetworkHandler;
import com.asangarin.packkit.PacketStatus;
import net.minecraft.server.v1_16_R3.PacketPlayInBlockDig;

public class BreakerNetworkHandler implements NetworkHandler {
	@Override
	public PacketStatus readBefore(Object packet) {
		if(packet instanceof PacketPlayInBlockDig) {
			System.out.println("=== BLOCK DIG PACKET ==");
			PacketPlayInBlockDig dig = (PacketPlayInBlockDig) packet;
			System.out.println("Boolean: " + dig.a());
			System.out.println("BlockPosition: " + dig.b());
			System.out.println("EnumDirection: " + dig.c());
			System.out.println("EnumPlayerDigType: " + dig.d());
			//return PacketStatus.DENY;
		}
		return PacketStatus.ALLOW;
	}

	@Override
	public PacketStatus writeBefore(Object packet) {
		return PacketStatus.ALLOW;
	}
}
