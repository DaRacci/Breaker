package eu.asangarin.breaker.comp.techtree;

import eu.asangarin.breaker.api.BreakerState;
import eu.asangarin.tt.TTPlugin;
import eu.asangarin.tt.exceptions.EntryNotFoundException;
import eu.asangarin.tt.player.PlayerData;
import io.lumine.mythic.bukkit.utils.config.LineConfig;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class TechEntryState extends BreakerState {
	private String entryKey;

	@Override
	public boolean isConditionMet(Player breaker, Block block) {
		PlayerData playerData = PlayerData.get(breaker);
		return playerData.hasEntry(entryKey);
	}

	@Override
	protected boolean setup(LineConfig config) {
		entryKey = config.getString("entry");
		if (entryKey == null) return error("'techentry' is missing the entry arg!");
		if (!entryKey.contains(".")) return error("'techentry' entry arg has an invalid format!!");
		try {
			TTPlugin.plugin.findEntry(entryKey);
			return true;
		} catch (EntryNotFoundException e) {
			return error("'techentry' couldn't find entry: '" + entryKey + "'!");
		}
	}
}
