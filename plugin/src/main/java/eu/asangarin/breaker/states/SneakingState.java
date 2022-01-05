package eu.asangarin.breaker.states;

import eu.asangarin.breaker.api.BreakerState;
import io.lumine.mythic.utils.config.LineConfig;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class SneakingState extends BreakerState {
	@Override
	public boolean isConditionMet(Player breaker, Block block) {
		return breaker.isSneaking();
	}
}
