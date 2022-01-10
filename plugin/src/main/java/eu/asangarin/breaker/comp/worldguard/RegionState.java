package eu.asangarin.breaker.comp.worldguard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import eu.asangarin.breaker.api.BreakerState;
import io.lumine.mythic.utils.config.LineConfig;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class RegionState extends BreakerState {
	private String region;

	@Override
	public boolean isConditionMet(Player breaker, Block block) {
		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		ApplicableRegionSet regionSet = container.createQuery().getApplicableRegions(BukkitAdapter.adapt(block.getLocation()));
		return regionSet.getRegions().stream().anyMatch((r) -> r.getId().equals(region));
	}

	@Override
	protected boolean setup(LineConfig config) {
		region = config.getString("name");
		if (region == null) return error("'region' is missing the name arg!");
		return true;
	}
}
