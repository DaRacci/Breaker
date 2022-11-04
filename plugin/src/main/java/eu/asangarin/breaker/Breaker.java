package eu.asangarin.breaker;

import eu.asangarin.breaker.cmd.BreakerCommand;
import eu.asangarin.breaker.comp.ExternalCompat;
import eu.asangarin.breaker.comp.crucible.CrucibleBlockProvider;
import eu.asangarin.breaker.comp.mmoitems.MIBlockProvider;
import eu.asangarin.breaker.comp.vault.VaultCompat;
import eu.asangarin.breaker.network.BreakerNetworkHandler;
import eu.asangarin.breaker.registry.BlockProviderRegistry;
import eu.asangarin.breaker.registry.BreakerStateRegistry;
import eu.asangarin.breaker.system.BreakingSystem;
import eu.asangarin.breaker.util.BreakerSettings;
import eu.asangarin.breaker.util.Metrics;
import eu.asangarin.packkit.Packkit;
import io.lumine.mythic.bukkit.utils.plugin.LuminePlugin;
import lombok.Getter;
import org.bukkit.Bukkit;
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
	private final BreakerSettings settings = new BreakerSettings();

	private final BlockProviderRegistry blockProviders = new BlockProviderRegistry();
	private final BreakerStateRegistry breakerStates = new BreakerStateRegistry();

	private boolean wgSupport;

	@Override
	public void load() {
		// Initialize singleton
		plugin = this;
		handleDefaultFiles();
		new Metrics(5790);

	}

	@Override
	public void enable() {
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
		wgSupport = getServer().getPluginManager().isPluginEnabled("WorldGuard");

		loadCompat("MythicLib", ExternalCompat::loadMythicLib);
		loadCompat("MMOCore", ExternalCompat::loadMMOCore);
		loadCompat("TechTree", ExternalCompat::loadTechTree);
		loadCompat("Vault", VaultCompat::setup);
		if (wgSupport) ExternalCompat.loadWorldGuard();
		loadCompat("MMOItems", () -> blockProviders.register(new MIBlockProvider()));
		loadCompat("MythicCrucible", () -> blockProviders.register(new CrucibleBlockProvider()));

		// Initialize breaking system
		breakingSystem.load();
		reload();
	}

	private void loadCompat(String pl, Runnable method) {
		if (getServer().getPluginManager().isPluginEnabled(pl)) method.run();
	}

	public void reload() {
		reloadConfiguration();
		database.reload();
		reloadConfig();
		settings.setup(getConfig());
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
