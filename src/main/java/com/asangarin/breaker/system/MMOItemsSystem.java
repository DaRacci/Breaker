package com.asangarin.breaker.system;

import org.bukkit.block.Block;

import com.asangarin.breaker.utility.BreakerSystem;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.CustomBlock;

public class MMOItemsSystem implements BreakerSystem {
	@Override
	public String getId(Block block) {
		CustomBlock custom = null;
		if(MMOItems.plugin.getCustomBlocks().isMushroomBlock(block.getType()))
			custom = CustomBlock.getFromData(block.getBlockData());
		
		if(custom != null) return "mmoitems_" + custom.getId();
		else return "invalid";
	}

	@Override
	public int priority() {
		return 5;
	}
}
