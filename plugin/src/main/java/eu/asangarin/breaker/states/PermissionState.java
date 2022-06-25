package eu.asangarin.breaker.states;

import eu.asangarin.breaker.api.BreakerState;
import io.lumine.mythic.bukkit.utils.config.LineConfig;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class PermissionState extends BreakerState {
	private String perm;

	@Override
	public boolean isConditionMet(Player breaker, Block block) {
		return breaker.hasPermission(perm);
	}

	@Override
	protected boolean setup(LineConfig config) {
		perm = config.getString("node");
		if (perm == null) return error("'permission' is missing the node arg!");
		return true;
	}
}
