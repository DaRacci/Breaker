package eu.asangarin.breaker.comp;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Location;

import java.util.List;

public class WorldGuardCompat {
	public static boolean checkRegions(List<String> regions, Location loc, boolean whitelist) {
		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		ApplicableRegionSet regionSet = container.createQuery().getApplicableRegions(BukkitAdapter.adapt(loc));
		if (whitelist) {
			for (ProtectedRegion region : regionSet)
				if (regions.contains(region.getId())) return true;
			return false;
		} else {
			for (ProtectedRegion region : regionSet)
				if (regions.contains(region.getId())) return false;
		}
		return true;
	}
}
