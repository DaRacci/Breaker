package eu.asangarin.breaker.states.mythiclib;

import eu.asangarin.breaker.api.BreakerState;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.utils.config.LineConfig;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class MMOStatState extends BreakerState {
	private String stat;
	private double value;
	private boolean higherThan;

	@Override
	public boolean isConditionMet(Player breaker, Block block) {
		MMOPlayerData mmoPlayer = MMOPlayerData.get(breaker);
		double statVal = mmoPlayer.getStatMap().getStat(stat);

		return higherThan ? statVal >= value : statVal == value;
	}

	@Override
	protected boolean setup(LineConfig config) {
		stat = config.getString("stat");
		if (stat == null) return error("'mmostat' is missing the stat arg!");
		this.value = config.getDouble("value");
		this.higherThan = config.getBoolean("higherthan");
		return true;
	}
}
