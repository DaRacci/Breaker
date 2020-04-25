package com.asangarin.breaker.states;

import org.bukkit.configuration.ConfigurationSection;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.api.BreakState;
import com.asangarin.breaker.core.BreakingBlock;

public class WorldState implements BreakState {
	private int value;
	private String worldname;
	
	public WorldState register(String wn, int val) {
		this.worldname = wn;
		this.value = val;
		return this;
	}

	@Override
	public String type() {
		return "world";
	}
	
	@Override
	public boolean activeState(BreakingBlock block) {
		Breaker.debug("World Test: " + block.getBlock().getWorld().getName().toLowerCase() + " | " + worldname, 6);
		if(block.getBlock().getWorld().getName().equalsIgnoreCase(worldname)) return true;
		return false;
	}

	@Override
	public int getStateValue(BreakingBlock block) {
		return value;
	}

	@Override
	public BreakState register(ConfigurationSection c) {
		return register(c.getString("world", "world").toLowerCase(), c.getInt("hardness", 1));
	}
}
