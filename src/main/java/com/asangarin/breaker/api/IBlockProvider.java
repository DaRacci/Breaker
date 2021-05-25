package com.asangarin.breaker.api;

import org.bukkit.block.Block;

public interface IBlockProvider {
	boolean check(Block block);
	String getKey(Block block);
}
