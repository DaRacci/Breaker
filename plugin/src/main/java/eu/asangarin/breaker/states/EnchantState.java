package eu.asangarin.breaker.states;

import eu.asangarin.breaker.api.BreakerState;
import io.lumine.mythic.utils.config.LineConfig;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.Map;

public class EnchantState extends BreakerState {
	private Enchantment type;
	private int level;

	@Override
	public boolean isConditionMet(Player breaker, Block block) {
		Map<Enchantment, Integer> enchants = breaker.getInventory().getItemInMainHand().getEnchantments();
		boolean flag = enchants.containsKey(type);
		if (flag && level != -1) return enchants.get(type) >= level;
		return flag;
	}

	@Override
	protected boolean setup(LineConfig config) {
		String type = config.getString("type");
		if (type == null) return error("'enchant' is missing the type arg!");
		NamespacedKey key = getFromString(type);
		if (key == null) return error("'enchant' type is invalid! couldn't read key: " + type);
		this.type = Enchantment.getByKey(key);
		this.level = config.getInteger("level", -1);
		return true;
	}

	@SuppressWarnings("deprecation")
	private NamespacedKey getFromString(String type) {
		if (type.contains(":")) {
			String[] split = type.split(":");
			return new NamespacedKey(split[0], split[1]);
		} else return NamespacedKey.minecraft(type);
	}
}
