package eu.asangarin.breaker.comp.mmocore;

import eu.asangarin.breaker.api.BreakerState;
import io.lumine.mythic.utils.config.LineConfig;
import net.Indyuce.mmocore.api.player.PlayerData;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.function.Function;

public class ResourceState extends BreakerState {
	private ResourceType type;
	private double amount = 0.0D;

	@Override
	public boolean isConditionMet(Player breaker, Block block) {
		Function<PlayerData, Double> func = getResource();
		return func.apply(PlayerData.get(breaker)) >= amount;
	}

	private Function<PlayerData, Double> getResource() {
		return switch (type) {
			case MANA -> PlayerData::getMana;
			case STAMINA -> PlayerData::getStamina;
			case STELLIUM -> PlayerData::getStellium;
		};
	}

	@Override
	protected boolean setup(LineConfig config) {
		String type = config.getString("type").toUpperCase();
		if (type == null) return error("'mmoresource' is missing the type arg!");
		if(!type.equals("MANA") && !type.equals("STAMINA") && !type.equals("STELLIUM"))
			return error("'mmoresource' is using an invalid type!");
		this.type = ResourceType.valueOf(type);
		amount = config.getDouble("amount");
		if (amount == 0.0D) return error("'mmoresource' is missing the amount arg (or it is 0)!");
		return true;
	}

	enum ResourceType {
		MANA, STAMINA, STELLIUM
	}
}
