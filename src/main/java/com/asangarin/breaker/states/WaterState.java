package com.asangarin.breaker.states;

import org.bukkit.configuration.ConfigurationSection;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.core.BreakingBlock;
import com.asangarin.breaker.utility.BreakState;

public class WaterState implements BreakState {
	private int value;
	
	public WaterState register(int val) {
		this.value = val;
		return this;
	}

	@Override
	public String type() {
		return "inwater";
	}
	
	@Override
	public boolean activeState(BreakingBlock block) {
		Breaker.debug("Water Test: " + block.getBreaker().isSwimming(), 6);
		if(block.getBreaker().isSwimming()) return true;
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
