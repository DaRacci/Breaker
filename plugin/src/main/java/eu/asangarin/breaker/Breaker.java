package eu.asangarin.breaker;

import eu.asangarin.breaker.cmd.BreakerCommand;
import eu.asangarin.breaker.network.BreakerNetworkHandler;
import eu.asangarin.breaker.registry.BlockProviderRegistry;
import eu.asangarin.breaker.registry.BreakerStateRegistry;
import eu.asangarin.breaker.system.BreakingSystem;
import eu.asangarin.breaker.util.BreakerRules;
import eu.asangarin.packkit.Packkit;
import io.lumine.mythic.utils.plugin.LuminePlugin;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@Getter
public class Breaker extends LuminePlugin {
	private static Breaker plugin;
	private final Packkit packkit = new Packkit(new BreakerNetworkHandler());
	private final BreakingSystem breakingSystem = new BreakingSystem();
	private final Database database = new Database();
	private final BreakerRules rules = new BreakerRules();

	private final BlockProviderRegistry blockProviders = new BlockProviderRegistry();
	private final BreakerStateRegistry breakerStates = new BreakerStateRegistry();

	private boolean mmSupport, wgSupport;

	@Override
	public void enable() {
		// Initialize singleton
		plugin = this;
		handleDefaultFiles();

		// Register Breaker Command
		registerCommand("breaker", new BreakerCommand());

		// Register Event Handlers
		registerListener(packkit);
		registerListener(breakingSystem);

		/* In the rare event that players are online before the plugins has enabled,
		make sure that they are properly registered by Packkit. */
		for (Player player : Bukkit.getServer().getOnlinePlayers())
			packkit.inject(player);

		// Cross-plugin compat
		mmSupport = getServer().getPluginManager().isPluginEnabled("MythicMobs");
		wgSupport = getServer().getPluginManager().isPluginEnabled("WorldGuard");

		// Initialize breaking system
		breakingSystem.load();
		reload();
	}

	public void reload() {
		reloadConfiguration();
		database.reload();
		reloadConfig();
		FileConfiguration config = getConfig();
		rules.setup(config.getConfigurationSection("breaker-rules"));
	}

	@Override
	public void disable() {
		// Make sure the singleton is nulled when the plugin disables
		plugin = null;

		// Unregister all online players from the Packkit duplex
		for (Player player : getServer().getOnlinePlayers())
			packkit.close(player);
	}

	private void handleDefaultFiles() {
		if (!new File(getDataFolder(), "config.yml").exists()) {
			File defaultDir = new File(getDataFolder(), "blocks");
			if (!defaultDir.isDirectory()) {
				if (defaultDir.mkdirs()) {
					File file = new File(getDataFolder(), "blocks/example.yml");
					InputStream link = getClass().getResourceAsStream("/plugindir/example.yml");
					if (link == null) {
						error("Something went incredibly wrong");
						return;
					}
					try {
						Files.copy(link, file.getAbsoluteFile().toPath());
					} catch (IOException e) {
						error("Couldn't copy default file...");
						e.printStackTrace();
					}
				} else error("Couldn't create default directories...");
			}
			saveDefaultConfig();
		}
	}

	public static Breaker get() {
		return plugin;
	}

	public static void error(String error) {
		plugin.getLogger().severe(error);
	}

	public static void log(String message) {
		plugin.getLogger().info(message);
	}
}
