package com.asangarin.breaker.system;

import org.bukkit.Material;
import org.bukkit.block.Block;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.api.BreakerSystem;

public class SkullSystem implements BreakerSystem {
	@Override
	public String getId(Block block) {
		if(block.getType() != Material.PLAYER_HEAD &&
			block.getType() != Material.PLAYER_WALL_HEAD) return "invalid";
		String skullValue = Breaker.plugin.nms.getSkullValue(block);
		if(skullValue.equals("invalid")) return "invalid";
		return "skull_" + skullValue;
	}

	@Override
	public int priority() {
		return 5;
	}
}
