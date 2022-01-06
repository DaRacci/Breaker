package eu.asangarin.breaker.comp.mythicmobs;

import eu.asangarin.breaker.api.BreakerState;
import io.lumine.mythic.utils.config.LineConfig;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractPlayer;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class MythicVariableState extends BreakerState {
	private String key;
	private int number;
	private boolean higherThan;

	@Override
	public boolean isConditionMet(Player breaker, Block block) {
		AbstractPlayer player = BukkitAdapter.adapt(breaker);
		int value = MythicMobs.inst().getPlayerManager().getPlayerData(player).getVariables().getInt(key);

		return higherThan ? value >= number : value == number;
	}

	@Override
	protected boolean setup(LineConfig config) {
		key = config.getString("key");
		if (key == null) return error("'mmvar' is missing the key arg!");
		this.number = config.getInteger("number");
		this.higherThan = config.getBoolean("higherthan");
		return true;
	}
}
