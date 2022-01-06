package eu.asangarin.breaker.comp.mmocore;

import eu.asangarin.breaker.api.BreakerState;
import io.lumine.mythic.utils.config.LineConfig;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.player.PlayerData;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class ClassState extends BreakerState {
	private String name;

	@Override
	public boolean isConditionMet(Player breaker, Block block) {
		PlayerData data = PlayerData.get(breaker);
		return data.getProfess().getId().equalsIgnoreCase(name);
	}

	@Override
	protected boolean setup(LineConfig config) {
		name = config.getString("name");
		if (name == null) return error("'mmoclass' is missing the name arg!");
		if (!MMOCore.plugin.classManager.has(name)) return error("'mmoclass' is using an invalid class!");
		return true;
	}
}
