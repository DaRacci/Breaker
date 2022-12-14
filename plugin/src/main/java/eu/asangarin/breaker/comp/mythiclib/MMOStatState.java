package eu.asangarin.breaker.comp.mythiclib;

import eu.asangarin.breaker.api.BreakerState;
import io.lumine.mythic.bukkit.utils.config.LineConfig;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class MMOStatState extends BreakerState {
	private String stat;
	private double statValue;
	private boolean higherThan;

	@Override
	public boolean isConditionMet(Player breaker, Block block) {
		MMOPlayerData mmoPlayer = MMOPlayerData.get(breaker);
		double statVal = mmoPlayer.getStatMap().getStat(stat);

		return higherThan ? statVal >= statValue : statVal == statValue;
	}

	@Override
	protected boolean setup(LineConfig config) {
		stat = config.getString("stat");
		if (stat == null) return error("'mmostat' is missing the stat arg!");
		this.statValue = config.getDouble("statval");
		this.higherThan = config.getBoolean("higherthan");
		return true;
	}
}
