package com.asangarin.breaker.api;

import org.bukkit.block.Block;

public interface BreakerSystem {
	int priority();
	String getId(Block block);
}
