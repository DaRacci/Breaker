package com.asangarin.breaker.api;

import org.bukkit.configuration.ConfigurationSection;

import com.asangarin.breaker.core.BreakingBlock;

public interface BreakState {
	String type();
	boolean activeState(BreakingBlock block);
	int getStateValue(BreakingBlock block);
	BreakState register(ConfigurationSection c);
}
