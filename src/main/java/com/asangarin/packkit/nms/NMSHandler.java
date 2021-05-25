package com.asangarin.packkit.nms;

import com.asangarin.breaker.network.BlockDigPacketInfo;
import io.netty.channel.Channel;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;

public interface NMSHandler {
	Channel getChannel(Player player);

	BlockDigPacketInfo readBlockDigPacket(Player player, Object packet);

	void breakBlock(BlockDigPacketInfo info);

	List<Material> getExcludedBlocks();

	String getSkullValue(Block block);

	Object createDigAnimationPacket(BlockDigPacketInfo info, int stage);
}
