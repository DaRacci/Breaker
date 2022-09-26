package eu.asangarin.breaker.util;

import eu.asangarin.breaker.Breaker;
import eu.asangarin.breaker.comp.worldguard.WorldGuardCompat;
import eu.asangarin.breaker.network.BlockDigPacketInfo;
import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BreakerSettings {
	private final BreakerRules rules = new BreakerRules();
	private final PermanentFatigue fatigue = new PermanentFatigue();
	private final SuperSecretSettings secret = new SuperSecretSettings();

	public void setup(ConfigurationSection config) {
		rules.setup(config.getConfigurationSection("breaker-rules"));
		fatigue.setup(config.getConfigurationSection("permanent-fatigue"));
		if (config.contains("super-secret-settings")) secret.setup(config.getConfigurationSection("super-secret-settings"));
	}

	public static class BreakerRules {
		private final List<String> worlds = new ArrayList<>();
		private final List<String> regions = new ArrayList<>();

		private boolean permissionRuleEnabled, worldRuleEnabled, worldWhitelist, regionRuleEnabled, regionWhitelist;

		protected void setup(ConfigurationSection section) {
			permissionRuleEnabled = section.getBoolean("permission.enabled");
			worldRuleEnabled = section.getBoolean("world.enabled");
			if (section.getBoolean("region.enabled")) {
				if (Breaker.get().isWgSupport()) {
					regionRuleEnabled = true;
				} else {
					regionRuleEnabled = false;
					Breaker.error("Couldn't enable region rule. WorldGuard not found.");
				}
			}

			worldWhitelist = section.getBoolean("world.whitelist");
			regionWhitelist = section.getBoolean("region.whitelist");

			worlds.addAll(section.getStringList("world.list"));
			regions.addAll(section.getStringList("region.list"));
		}

		public boolean test(BlockDigPacketInfo info) {
			return test(info.getPlayer().get(), info.getBlock());
		}

		public boolean test(Player player, Block block) {
			if (player == null) return false;

			if (permissionRuleEnabled && !player.hasPermission("breaker.use")) return false;

			if (worldRuleEnabled) {
				boolean flag;
				String world = block.getWorld().getName();
				if (worldWhitelist) flag = worlds.contains(world);
				else flag = !worlds.contains(world);

				if (!flag) return false;
			}

			if (regionRuleEnabled) return WorldGuardCompat.checkRegions(regions, block.getLocation(), regionWhitelist);

			return true;
		}
	}

	public static class PermanentFatigue {
		@Getter
		private boolean isEnabled;
		private boolean isWhitelist;
		private final List<String> worlds = new ArrayList<>();

		protected void setup(ConfigurationSection section) {
			isEnabled = section.getBoolean("enabled");
			worlds.clear();
			worlds.addAll(section.getStringList("worlds"));
			isWhitelist = section.getBoolean("whitelist");
		}

		public boolean test(String name) {
			return isEnabled && testWorld(name);
		}

		public boolean testWorld(String name) {
			return isWhitelist == worlds.contains(name);
		}
	}

	public static class SuperSecretSettings {
		@Getter
		private boolean ignoreZeroPackets;

		protected void setup(ConfigurationSection section) {
			ignoreZeroPackets = section.getBoolean("ignore-zero-packets");
		}
	}

	public static BreakerSettings get() {
		return Breaker.get().getSettings();
	}
}
