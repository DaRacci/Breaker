package eu.asangarin.breaker.states;

import eu.asangarin.breaker.api.BreakerState;
import io.lumine.mythic.bukkit.utils.config.LineConfig;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class EffectState extends BreakerState {
	private PotionEffectType type;
	private int level;

	@Override
	public boolean isConditionMet(Player breaker, Block block) {
		boolean flag = breaker.hasPotionEffect(type);
		if (flag && level != -1) return breaker.getPotionEffect(type).getAmplifier() >= level;
		return flag;
	}

	@Override
	protected boolean setup(LineConfig config) {
		String type = config.getString("type");
		if (type == null) return error("'effect' is missing the type arg!");
		this.type = PotionEffectType.getByName(type);
		this.level = config.getInteger("level", -1);
		return true;
	}
}
