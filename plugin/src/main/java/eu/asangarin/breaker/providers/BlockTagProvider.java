package eu.asangarin.breaker.providers;

import eu.asangarin.breaker.Breaker;
import eu.asangarin.breaker.api.IBlockProvider;
import org.bukkit.block.Block;

import java.util.Collection;

public class BlockTagProvider implements IBlockProvider {
	@Override
	public Collection<String> getKeys(Block block) {
		return Breaker.get().getPackkit().getNMS().getTagsFor(block);
	}
}
