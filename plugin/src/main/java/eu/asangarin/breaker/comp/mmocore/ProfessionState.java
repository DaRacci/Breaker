package eu.asangarin.breaker.comp.mmocore;

import eu.asangarin.breaker.api.BreakerState;
import io.lumine.mythic.utils.config.LineConfig;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.player.PlayerData;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class ProfessionState extends BreakerState {
	private String prof;
	private float level = 0.0F;

	@Override
	public boolean isConditionMet(Player breaker, Block block) {
		PlayerData data = PlayerData.get(breaker);
		return data.getCollectionSkills().getLevel(prof) >= level;
	}

	@Override
	protected boolean setup(LineConfig config) {
		prof = config.getString("name");
		if (prof == null) return error("'mmoprof' is missing the name arg!");
		if (!MMOCore.plugin.professionManager.has(prof)) return error("'mmoprof' is using an invalid profession!");
		level = config.getInteger("level");
		if (level == 0.0F) return error("'mmoprof' is missing the level arg (or it is 0)!");
		return true;
	}
}
