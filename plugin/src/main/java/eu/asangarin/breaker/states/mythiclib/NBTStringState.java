package eu.asangarin.breaker.states.mythiclib;

import eu.asangarin.breaker.api.BreakerState;
import io.lumine.mythic.lib.api.item.NBTItem;
import io.lumine.mythic.utils.config.LineConfig;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class NBTStringState extends BreakerState {
	private String key;
	private String value;

	@Override
	public boolean isConditionMet(Player breaker, Block block) {
		NBTItem nbt = NBTItem.get(breaker.getInventory().getItemInMainHand());
		return nbt.getString(key).equals(value);
	}

	@Override
	protected boolean setup(LineConfig config) {
		key = config.getString("key");
		if (key == null) return error("'nbtstr' is missing the key arg!");
		value = config.getString("value");
		if (value == null) return error("'nbtstr' is missing the value arg!");
		return true;
	}
}
