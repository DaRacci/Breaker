package com.asangarin.breaker.states;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.api.BreakerState;
import io.lumine.mythic.utils.config.LineConfig;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class HeldItemState extends BreakerState {
	private Material material = Material.BEDROCK;

	@Override
	public boolean isConditionMet(Player breaker, Block block) {
		return breaker.getInventory().getItemInMainHand().getType() == material;
	}

	@Override
	public void setup(LineConfig config) {
		String mat = config.getString("type");
		if (mat == null) {
			Breaker.error("Line: ( " + config.getLine() + " )");
			Breaker.error("The 'type' argument was null");
			return;
		}

		try {
			material = Material.valueOf(mat.toUpperCase());
		} catch (Exception e) {
			Breaker.error("'" + mat + "' is not a valid material");
		}
	}
}
