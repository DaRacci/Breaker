package eu.asangarin.breaker.api;

import org.bukkit.block.Block;

import java.util.Collection;

public interface IBlockProvider {
	Collection<String> getKeys(Block block);
}
