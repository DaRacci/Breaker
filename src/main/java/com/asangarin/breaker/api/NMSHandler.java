package com.asangarin.breaker.api;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.asangarin.breaker.Breaker;

public abstract class NMSHandler {
	public NMSHandler() {
		Breaker.debug("Loaded NMS Version: " + getVersion(), 0);
	}
	
	public abstract String getVersion();
	public abstract void breakBlock(Player player, Location loc);
	public abstract Sound getBlockBreakSound(Block block);
	public abstract List<Material> getExlcudedBlocks();
}
