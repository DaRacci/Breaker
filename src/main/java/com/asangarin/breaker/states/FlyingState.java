package com.asangarin.breaker.states;

import org.bukkit.configuration.ConfigurationSection;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.api.BreakState;
import com.asangarin.breaker.core.BreakingBlock;

public class FlyingState implements BreakState {
	private int value;
	
	public FlyingState register(int val) {
		this.value = val;
		return this;
	}

	@Override
	public String type() {
		return "inair";
	}
	
	@Override
	public boolean activeState(BreakingBlock block) {
		Breaker.debug("Flying Test: " + !block.getBreaker().isOnGround(), 6);
		if(!block.getBreaker().isOnGround()) return true;
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
