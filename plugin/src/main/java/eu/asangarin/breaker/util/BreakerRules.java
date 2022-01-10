package eu.asangarin.breaker.util;

import eu.asangarin.breaker.Breaker;
import eu.asangarin.breaker.comp.worldguard.WorldGuardCompat;
import eu.asangarin.breaker.network.BlockDigPacketInfo;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BreakerRules {
	private final List<String> worlds = new ArrayList<>();
	private final List<String> regions = new ArrayList<>();

	private boolean permissionRuleEnabled, worldRuleEnabled, worldWhitelist, regionRuleEnabled, regionWhitelist;

	public void setup(ConfigurationSection section) {
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
		Player player = info.getPlayer().get();
		if (player == null) return false;
		Block block = info.getBlock();

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
