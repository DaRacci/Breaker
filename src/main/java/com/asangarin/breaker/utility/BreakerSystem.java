package com.asangarin.breaker.utility;

import org.bukkit.block.Block;

public interface BreakerSystem {
	int priority();
	String getId(Block block);
}
