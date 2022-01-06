package eu.asangarin.breaker.states;

import eu.asangarin.breaker.api.BreakerState;
import io.lumine.mythic.utils.config.LineConfig;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class WeatherState extends BreakerState {
	private boolean thundering;

	@Override
	public boolean isConditionMet(Player breaker, Block block) {
		World world = block.getWorld();
		return thundering ? world.isThundering() : world.hasStorm();
	}

	@Override
	protected boolean setup(LineConfig config) {
		thundering = config.getBoolean("thunder", false);
		return true;
	}
}
