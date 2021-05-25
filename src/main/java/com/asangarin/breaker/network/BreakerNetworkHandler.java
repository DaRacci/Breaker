package com.asangarin.breaker.network;

import com.asangarin.breaker.Breaker;
import com.asangarin.packkit.NetworkHandler;
import com.asangarin.packkit.PacketStatus;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class BreakerNetworkHandler implements NetworkHandler {
	@Override
	public PacketStatus readBefore(Player player, Object packet) {
		if(player.getGameMode() == GameMode.CREATIVE) return PacketStatus.ALLOW;
		BlockDigPacketInfo info = Breaker.get().getPackkit().getNMS().readBlockDigPacket(player, packet);
		if (info == null) return PacketStatus.ALLOW;

		System.out.println(info);
		return Breaker.get().getBreakingSystem().compute(info) ? PacketStatus.ALLOW : PacketStatus.DENY;
	}

	@Override
	public void readAfter(Player player, Object packet) {
		if(player.getGameMode() == GameMode.CREATIVE) return;
		BlockDigPacketInfo info = Breaker.get().getPackkit().getNMS().readBlockDigPacket(player, packet);
		if (info == null) return;

		System.out.println(info);
		Breaker.get().getBreakingSystem().conclude(info);
	}
}
