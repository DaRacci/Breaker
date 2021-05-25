package com.asangarin.breaker.registry;

import com.asangarin.breaker.api.IBlockProvider;
import com.asangarin.breaker.blockproviders.MaterialBlockProvider;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class BlockProviderRegistry {
	private final List<IBlockProvider> providers = new ArrayList<>();

	public BlockProviderRegistry() {
		register(new MaterialBlockProvider());
	}

	public void register(IBlockProvider provider) {
		providers.add(provider);
	}

	public List<String> getKeysFromBlock(Block block) {
		List<String> list = new ArrayList<>();
		for(IBlockProvider provider : providers)
			if(provider.check(block))
				list.add(provider.getKey(block));
		return list;
	}
}
