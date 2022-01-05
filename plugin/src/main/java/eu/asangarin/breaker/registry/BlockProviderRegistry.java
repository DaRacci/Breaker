package eu.asangarin.breaker.registry;

import eu.asangarin.breaker.api.IBlockProvider;
import eu.asangarin.breaker.providers.BlockTagProvider;
import eu.asangarin.breaker.providers.MaterialTypeProvider;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class BlockProviderRegistry {
	private final List<IBlockProvider> providers = new ArrayList<>();

	public BlockProviderRegistry() {
		register(new MaterialTypeProvider());
		register(new BlockTagProvider());
	}

	public void register(IBlockProvider provider) {
		providers.add(provider);
	}

	public List<String> getKeysFromBlock(Block block) {
		List<String> list = new ArrayList<>();
		for(IBlockProvider provider : providers)
			list.addAll(provider.getKeys(block));
		return list;
	}
}
