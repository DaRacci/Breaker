package com.asangarin.breaker.manager;

import java.util.HashMap;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.core.BlockConfiguration;

public class Database {
	public HashMap<String, BlockConfiguration> blockConfigs = new HashMap<>();
	
	public void add(String config, BlockConfiguration block) {
		Breaker.debug("Added " + config.toLowerCase() + " to the Database!", 3);
		blockConfigs.put(config.toLowerCase(), block);
	}
	
	public BlockConfiguration get(String config) {
		Breaker.debug("Retrieving " + config.toLowerCase() + " from the Database!", 3);
		return blockConfigs.get(config.toLowerCase());
	}
	
	public void clear() {
		Breaker.debug("Cleared Database!", 4);
		blockConfigs.clear();
	}
	
	public boolean has(String config) {
		Breaker.debug("Checking if " + config.toLowerCase() + " exists in Database... (" + blockConfigs.containsKey(config.toLowerCase()) + ")", 3);
		return blockConfigs.containsKey(config.toLowerCase());
	}
}
