package eu.asangarin.breaker.providers;

import eu.asangarin.breaker.api.IBlockProvider;
import org.bukkit.block.Block;

import java.util.Collection;
import java.util.Collections;

public class MaterialTypeProvider implements IBlockProvider {
	public Collection<String> getKeys(Block block) {
		return Collections.singletonList(block.getType().toString().toLowerCase());
	}
}
