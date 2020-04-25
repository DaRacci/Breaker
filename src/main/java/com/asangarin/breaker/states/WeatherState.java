package com.asangarin.breaker.states;

import org.bukkit.configuration.ConfigurationSection;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.api.BreakState;
import com.asangarin.breaker.core.BreakingBlock;

public class WeatherState implements BreakState {
	private int value;
	
	public WeatherState register(int val) {
		this.value = val;
		return this;
	}

	@Override
	public String type() {
		return "raining";
	}
	
	@Override
	public boolean activeState(BreakingBlock block) {
		Breaker.debug("Rain Test: " + block.getBlock().getWorld().hasStorm(), 6);
		if(block.getBlock().getWorld().hasStorm()) return true;
		return false;
	}

	@Override
	public int getStateValue(BreakingBlock block) {
		return value;
	}

	@Override
	public BreakState register(ConfigurationSection c) {
		return register(c.getInt("hardness", 1));
	}
}
