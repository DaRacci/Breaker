package com.asangarin.breaker.utility;

import org.bukkit.configuration.ConfigurationSection;

import com.asangarin.breaker.core.BreakingBlock;

public interface BreakState {
	String type();
	boolean activeState(BreakingBlock block);
	int getStateValue(BreakingBlock block);
	BreakState register(ConfigurationSection c);
}
