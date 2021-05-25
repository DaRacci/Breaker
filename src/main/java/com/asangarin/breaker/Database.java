package com.asangarin.breaker;

import com.asangarin.breaker.system.DatabaseBlock;
import io.lumine.mythic.utils.config.properties.Property;
import org.bukkit.block.Block;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Database {
	private final HashMap<String, DatabaseBlock> blockConfigs = new HashMap<>();

	public void reload() {
		blockConfigs.clear();

		File files = new File(Breaker.get().getDataFolder(), "blocks");
		if (!files.isDirectory()) return;
		for (File file : files.listFiles()) {
			if (file.isDirectory() || !file.getName().substring(file.getName().lastIndexOf('.')).equalsIgnoreCase(".yml")) continue;

			Object fileName = "blocks/" + file.getName();
			blockConfigs.put(Property.String(Breaker.get(), fileName, "block").get().toLowerCase(), new DatabaseBlock(fileName));
		}
	}

	public void add(String config, DatabaseBlock block) {
		blockConfigs.put(config.toLowerCase(), block);
	}

	public Optional<DatabaseBlock> fromBlock(Block block) {
		List<String> keys = Breaker.get().getBlockProviders().getKeysFromBlock(block);
		for(String key : keys)
			if(blockConfigs.containsKey(key))
				return Optional.of(blockConfigs.get(key));
		return Optional.empty();
	}

	public boolean shouldBlockBeHandled(Block block) {
		List<String> keys = Breaker.get().getBlockProviders().getKeysFromBlock(block);
		for(String key : keys)
			if(blockConfigs.containsKey(key))
				return true;
		return false;
	}
}
