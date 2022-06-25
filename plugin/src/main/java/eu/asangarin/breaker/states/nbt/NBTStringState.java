package eu.asangarin.breaker.states.nbt;

import eu.asangarin.breaker.api.BreakerState;
import eu.asangarin.breaker.util.NBTUtil;
import io.lumine.mythic.bukkit.utils.config.LineConfig;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class NBTStringState extends BreakerState {
	private String key;
	private String nbtValue;

	@Override
	public boolean isConditionMet(Player breaker, Block block) {
		String value = NBTUtil.getStringValue(key, breaker.getInventory().getItemInMainHand());
		return value.equals(nbtValue);
	}

	@Override
	protected boolean setup(LineConfig config) {
		key = config.getString("key");
		if (key == null) return error("'nbtstr' is missing the key arg!");
		nbtValue = config.getString("nbtval");
		if (nbtValue == null) return error("'nbtstr' is missing the value arg!");
		return true;
	}
}
