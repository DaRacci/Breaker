package eu.asangarin.breaker.states;

import eu.asangarin.breaker.api.BreakerState;
import io.lumine.mythic.bukkit.utils.config.LineConfig;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class WorldState extends BreakerState {
	private String world;

	@Override
	public boolean isConditionMet(Player breaker, Block block) {
		return block.getWorld().getName().equalsIgnoreCase(world);
	}

	@Override
	protected boolean setup(LineConfig config) {
		world = config.getString("name");
		if (world == null) return error("'world' is missing the name arg!");
		return true;
	}
}
