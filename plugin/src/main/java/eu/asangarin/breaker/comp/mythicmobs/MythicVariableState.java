package eu.asangarin.breaker.comp.mythicmobs;

import eu.asangarin.breaker.api.BreakerState;
import io.lumine.mythic.api.adapters.AbstractPlayer;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.utils.config.LineConfig;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class MythicVariableState extends BreakerState {
	private String key;
	private int varValue;
	private boolean higherThan;

	@Override
	public boolean isConditionMet(Player breaker, Block block) {
		AbstractPlayer player = BukkitAdapter.adapt(breaker);
		int value = MythicBukkit.inst().getPlayerManager().getProfile(player).getVariables().getInt(key);

		return higherThan ? value >= varValue : value == varValue;
	}

	@Override
	protected boolean setup(LineConfig config) {
		key = config.getString("key");
		if (key == null) return error("'mmvar' is missing the key arg!");
		this.varValue = config.getInteger("varval");
		this.higherThan = config.getBoolean("higherthan");
		return true;
	}
}
