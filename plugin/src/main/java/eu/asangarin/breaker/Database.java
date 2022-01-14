package eu.asangarin.breaker;

import eu.asangarin.breaker.system.DatabaseBlock;
import eu.asangarin.breaker.util.BlockFile;
import io.lumine.mythic.utils.config.properties.Property;
import org.bukkit.block.Block;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Database {
	private final HashMap<String, DatabaseBlock> blockConfigs = new HashMap<>();

	public void reload() {
		Breaker.log("Loading Breaker Database...");
		blockConfigs.clear();

		File files = new File(Breaker.get().getDataFolder(), "blocks");
		if (!files.isDirectory()) return;
		loadFromDir(files);
	}

	private void loadFromDir(File files) {
		//noinspection ConstantConditions
		for (File file : files.listFiles()) {
			if (file.isDirectory()) {
				loadFromDir(file);
				continue;
			}
			if (!file.getName().substring(file.getName().lastIndexOf('.')).equalsIgnoreCase(".yml")) continue;

			BlockFile blockFile = new BlockFile("blocks/" + file.getName());
			blockConfigs.put(Property.String(blockFile, "block").get().toLowerCase(), new DatabaseBlock(blockFile));
		}
	}

	public Optional<DatabaseBlock> fromBlock(Block block) {
		List<String> keys = Breaker.get().getBlockProviders().getKeysFromBlock(block);
		for (String key : keys)
			if (blockConfigs.containsKey(key.toLowerCase())) return Optional.of(blockConfigs.get(key.toLowerCase()));
		return Optional.empty();
	}

	public boolean shouldBlockBeHandled(Block block) {
		List<String> keys = Breaker.get().getBlockProviders().getKeysFromBlock(block);
		for (String key : keys)
			if (blockConfigs.containsKey(key)) return true;
		return false;
	}
}
