package com.asangarin.breaker.utility;

import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public interface BreakTrigger {
	String type();
	void execute(Player player, Block block);
	BreakTrigger register(ConfigurationSection c);
}
