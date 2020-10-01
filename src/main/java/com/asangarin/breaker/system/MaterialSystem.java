package com.asangarin.breaker.system;

import org.bukkit.block.Block;

import com.asangarin.breaker.api.BreakerSystem;

public class MaterialSystem implements BreakerSystem {
	@Override
	public String getId(Block block) {
		return block.getType().name();
	}

	@Override
	public int priority() {
		return 10;
	}
}
