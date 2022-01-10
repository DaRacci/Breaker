package eu.asangarin.breaker.comp.mmocore;

import eu.asangarin.breaker.api.BreakerState;
import io.lumine.mythic.utils.config.LineConfig;
import net.Indyuce.mmocore.api.player.PlayerData;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.function.Function;

public class PointState extends BreakerState {
	private PointType type;
	private int amount = 0;

	@Override
	public boolean isConditionMet(Player breaker, Block block) {
		Function<PlayerData, Integer> func = getResource();
		return func.apply(PlayerData.get(breaker)) >= amount;
	}

	private Function<PlayerData, Integer> getResource() {
		switch (type) {
			case CLASS:
				return PlayerData::getClassPoints;
			case SKILL:
				return PlayerData::getSkillPoints;
			case ATTRIBUTE:
				return PlayerData::getAttributePoints;
			case RECOLLECTION:
				return PlayerData::getAttributeReallocationPoints;
			default:
				return (playerData -> 0);
		}
	}

	@Override
	protected boolean setup(LineConfig config) {
		String type = config.getString("type").toUpperCase();
		if (type == null) return error("'mmopoint' is missing the type arg!");
		if (!type.equals("CLASS") && !type.equals("SKILL") && !type.equals("ATTRIBUTE") && !type.equals("RECOLLECTION"))
			return error("'mmopoint' is using an invalid type!");
		this.type = PointType.valueOf(type);
		amount = config.getInteger("amount");
		if (amount == 0) return error("'mmopoint' is missing the amount arg (or it is 0)!");
		return true;
	}

	enum PointType {
		CLASS, SKILL, ATTRIBUTE, RECOLLECTION
	}
}
