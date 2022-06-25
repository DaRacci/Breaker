package eu.asangarin.breaker.states;

import eu.asangarin.breaker.api.BreakerState;
import io.lumine.mythic.bukkit.utils.config.LineConfig;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class HeldItemState extends BreakerState {
	private Material material;

	@Override
	public boolean isConditionMet(Player breaker, Block block) {
		return breaker.getInventory().getItemInMainHand().getType() == material;
	}

	@Override
	public boolean setup(LineConfig config) {
		String mat = config.getString("type");
		if (mat == null) return error("'helditem' is missing the type arg!");

		try {
			material = Material.valueOf(mat.toUpperCase());
		} catch (IllegalArgumentException e) {
			return error("Unknown material type in 'helditem' state!");
		}

		return true;
	}
}
