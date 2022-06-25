package eu.asangarin.breaker.comp.mmocore;

import eu.asangarin.breaker.api.BreakerState;
import io.lumine.mythic.bukkit.utils.config.LineConfig;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.api.player.attribute.PlayerAttribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class AttributeState extends BreakerState {
	private String attribute;
	private int level = 0;

	@Override
	public boolean isConditionMet(Player breaker, Block block) {
		PlayerAttribute playerAttribute = MMOCore.plugin.attributeManager.get(attribute);
		return PlayerData.get(breaker).getAttributes().getAttribute(playerAttribute) >= level;
	}

	@Override
	protected boolean setup(LineConfig config) {
		attribute = config.getString("name");
		if (attribute == null) return error("'mmoattribute' is missing the name arg!");
		if (!MMOCore.plugin.attributeManager.has(attribute)) return error("'mmoattribute' is using an invalid attribute!");
		level = config.getInteger("level");
		if (level == 0) return error("'mmoattribute' is missing the level arg (or it is 0)!");
		return true;
	}
}
