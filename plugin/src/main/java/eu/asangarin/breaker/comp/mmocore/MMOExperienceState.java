package eu.asangarin.breaker.comp.mmocore;

import eu.asangarin.breaker.api.BreakerState;
import io.lumine.mythic.utils.config.LineConfig;
import net.Indyuce.mmocore.api.player.PlayerData;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class MMOExperienceState extends BreakerState {
	private boolean level;
	private float amount = 0.0F;

	@Override
	public boolean isConditionMet(Player breaker, Block block) {
		PlayerData data = PlayerData.get(breaker);
		if (level) return data.getLevel() >= (int) amount;
		return data.getExperience() >= amount;
	}

	@Override
	protected boolean setup(LineConfig config) {
		level = config.getBoolean("level", false);
		amount = config.getFloat("amount");
		if (amount == 0.0F) return error("'mmoexp' is missing the amount arg (or it is 0)!");
		return true;
	}
}
