package com.asangarin.breaker.api;

import com.asangarin.breaker.Breaker;
import io.lumine.mythic.utils.config.LineConfig;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public abstract class BreakerState {
	protected int value = 1;

	public int getDeduction() {
		return value;
	}

	public abstract boolean isConditionMet(Player breaker, Block block);
	protected abstract void setup(LineConfig config);

	public void create(LineConfig config) {
		value = config.getInteger("value", 1);
		setup(config);
	}

	protected void invalidArgument(String arg) {
		Breaker.error("'" + arg + "'");
	}
}
