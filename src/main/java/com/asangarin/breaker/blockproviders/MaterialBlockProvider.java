package com.asangarin.breaker.blockproviders;

import com.asangarin.breaker.api.IBlockProvider;
import org.bukkit.block.Block;

public class MaterialBlockProvider implements IBlockProvider {
	@Override
	public boolean check(Block block) {
		return true;
	}

	public String getKey(Block block) {
		return block.getType().toString().toLowerCase();
	}
}
