package com.asangarin.packkit.nms;

import com.asangarin.breaker.network.BlockDigPacketInfo;
import com.google.gson.JsonObject;
import io.netty.channel.Channel;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class NMSHandler {
	private final JsonObject mappings = new JsonObject();

	public abstract Channel getChannel(Player player);
	public abstract BlockDigPacketInfo readBlockDigPacket(Object packet);
	public abstract void breakBlock(Player player, Location loc);
	public abstract Sound getBreakSound(Block block);
	public abstract List<Material> getExcludedBlocks();
	public abstract String getSkullValue(Block block);

	protected String getMapping(String mapping) {
		return mappings.get(mapping).getAsString();
	}
}
