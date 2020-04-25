package com.asangarin.breaker.system;

import java.util.Optional;

import org.bukkit.block.Block;

import com.asangarin.breaker.api.BreakerSystem;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.block.CustomBlock;

public class MMOItemsSystem implements BreakerSystem {
	@Override
	public String getId(Block block) {
		if(MMOItems.plugin.getCustomBlocks().isMushroomBlock(block.getType())) {
			Optional<CustomBlock> custom = MMOItems.plugin.getCustomBlocks().getFromBlock(block.getBlockData());
		
			if(custom.isPresent()) return "mmoitems_" + custom.get().getId();
		}
		
		return "invalid";
	}

	@Override
	public int priority() {
		return 5;
	}
}
