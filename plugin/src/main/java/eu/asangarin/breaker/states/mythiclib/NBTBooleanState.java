package eu.asangarin.breaker.states.mythiclib;

import eu.asangarin.breaker.api.BreakerState;
import io.lumine.mythic.lib.api.item.NBTItem;
import io.lumine.mythic.utils.config.LineConfig;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class NBTBooleanState extends BreakerState {
	private String key;
	private boolean value;

	@Override
	public boolean isConditionMet(Player breaker, Block block) {
		NBTItem nbt = NBTItem.get(breaker.getInventory().getItemInMainHand());
		return nbt.getBoolean(key) == value;
	}

	@Override
	protected boolean setup(LineConfig config) {
		key = config.getString("key");
		if (key == null) return error("'nbtbool' is missing the key arg!");
		this.value = config.getBoolean("value", true);
		return true;
	}
}
