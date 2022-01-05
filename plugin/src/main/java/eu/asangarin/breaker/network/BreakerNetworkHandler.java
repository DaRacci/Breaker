package eu.asangarin.breaker.network;

import eu.asangarin.breaker.Breaker;
import eu.asangarin.packkit.NetworkHandler;
import eu.asangarin.packkit.PacketStatus;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class BreakerNetworkHandler implements NetworkHandler {
	@Override
	public PacketStatus readBefore(Player player, Object packet) {
		if(player.getGameMode() == GameMode.CREATIVE) return PacketStatus.ALLOW;
		BlockDigPacketInfo info = Breaker.get().getPackkit().getNMS().readBlockDigPacket(player, packet);
		if (info == null || info.getDigType() == BlockDigPacketInfo.DigType.INVALID) return PacketStatus.ALLOW;

		return Breaker.get().getBreakingSystem().compute(info) ? PacketStatus.ALLOW : PacketStatus.DENY;
	}

	@Override
	public void readAfter(Player player, Object packet) {
		if(player.getGameMode() == GameMode.CREATIVE) return;
		BlockDigPacketInfo info = Breaker.get().getPackkit().getNMS().readBlockDigPacket(player, packet);
		if (info == null || info.getDigType() == BlockDigPacketInfo.DigType.INVALID) return;

		Breaker.get().getBreakingSystem().conclude(info);
	}
}
