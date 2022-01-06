package eu.asangarin.breaker.comp.techtree;

import eu.asangarin.breaker.api.BreakerState;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class TechEntryState extends BreakerState {
	@Override
	public boolean isConditionMet(Player breaker, Block block) {
		return false;
	}
}
