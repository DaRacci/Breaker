package eu.asangarin.breaker.states.nbt;

import eu.asangarin.breaker.api.BreakerState;
import eu.asangarin.breaker.util.NBTUtil;
import io.lumine.mythic.bukkit.utils.config.LineConfig;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class NBTBooleanState extends BreakerState {
	private String key;
	private boolean nbtValue;

	@Override
	public boolean isConditionMet(Player breaker, Block block) {
		boolean value = NBTUtil.getBooleanValue(key, breaker.getInventory().getItemInMainHand());
		return value == nbtValue;
	}

	@Override
	protected boolean setup(LineConfig config) {
		key = config.getString("key");
		if (key == null) return error("'nbtbool' is missing the key arg!");
		this.nbtValue = config.getBoolean("nbtval", true);
		return true;
	}
}
