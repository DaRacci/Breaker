package eu.asangarin.breaker.states;

import eu.asangarin.breaker.api.BreakerState;
import io.lumine.mythic.utils.config.LineConfig;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class ExperienceState extends BreakerState {
	private boolean level;
	private float amount = 0.0F;

	@Override
	public boolean isConditionMet(Player breaker, Block block) {
		if(level) return breaker.getLevel() >= (int) amount;
		return breaker.getExp() >= amount;
	}

	@Override
	protected boolean setup(LineConfig config) {
		level = config.getBoolean("level", false);
		amount = config.getFloat("amount");
		if (amount == 0.0F) return error("'exp' is missing the amount arg (or it is 0)!");
		return true;
	}
}
