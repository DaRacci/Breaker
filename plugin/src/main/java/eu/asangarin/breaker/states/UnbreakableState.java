package eu.asangarin.breaker.states;

import eu.asangarin.breaker.api.BreakerState;
import io.lumine.mythic.bukkit.utils.config.LineConfig;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class UnbreakableState extends BreakerState {
	@Override
	public boolean isConditionMet(Player breaker, Block block) {
		return false;
	}

	@Override
	protected boolean setup(LineConfig config) {
		required = true;
		return true;
	}
}
