package com.asangarin.breaker.triggers;

import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.asangarin.breaker.api.BreakTrigger;

public class FireTrigger implements BreakTrigger {
	private int ticks;
	
	FireTrigger register(int t) {
		this.ticks = t;
		return this;
	}
	
	@Override
	public String type() {
		return "fire";
	}
	
	@Override
	public void execute(Player player, Block block) {
		player.setFireTicks(ticks);
	}

	@Override
	public BreakTrigger register(ConfigurationSection c) {
		return register(c.getInt("ticks", 20));
	}
}
