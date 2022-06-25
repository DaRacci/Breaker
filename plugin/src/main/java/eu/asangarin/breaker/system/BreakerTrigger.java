package eu.asangarin.breaker.system;

import eu.asangarin.breaker.Breaker;
import eu.asangarin.breaker.api.BreakerTriggerEvent;
import eu.asangarin.breaker.comp.mythicmobs.MythicMobsCompat;
import eu.asangarin.breaker.util.TriggerFunction;
import eu.asangarin.breaker.util.TriggerType;
import io.lumine.mythic.bukkit.utils.config.LineConfig;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class BreakerTrigger {
	private TriggerType type;
	private TriggerFunction function;
	private String args;

	public boolean validate(String name, LineConfig config) {
		if (!TriggerType.TYPES.contains(config.getKey())) return error(name, "'" + config.getKey() + "' is not a valid type!");
		type = TriggerType.valueOf(config.getKey().toUpperCase());

		args = config.getString("command", "");
		if (!args.isEmpty()) {
			function = TriggerFunction.COMMAND;
			return true;
		}
		args = config.getString("event", "");
		if (!args.isEmpty()) {
			function = TriggerFunction.EVENT;
			return true;
		}
		args = config.getString("skill", "");
		if (!args.isEmpty()) {
			function = TriggerFunction.SKILL;
			return true;
		}
		return error(name, "Your '" + config.getKey() + "' trigger does not have a valid function!");
	}

	private boolean error(String name, String arg) {
		Breaker.error("[" + name + "] Couldn't add trigger: " + arg);
		return false;
	}

	public TriggerTrigger prepare() {
		switch (function) {
			case COMMAND: return (player, block) -> {
				String cmd = args.replace("%player%", player.getName());
				if (cmd.startsWith("!")) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.substring(1));
				else Bukkit.dispatchCommand(player, cmd);
			};
			case SKILL: return (player, block) -> MythicMobsCompat.castSkill(player, block.getLocation(), args);
			case EVENT: return (player, block) -> Bukkit.getPluginManager().callEvent(new BreakerTriggerEvent(player, block, args));
			default: return (player, block) -> {};
		}
	}

	public static Map<TriggerType, List<TriggerTrigger>> newTriggerMap() {
		Map<TriggerType, List<TriggerTrigger>> triggers = new HashMap<>();
		for (TriggerType type : TriggerType.ALL)
			triggers.put(type, new ArrayList<>());
		return triggers;
	}

	@FunctionalInterface
	interface TriggerTrigger {
		void trigger(Player player, Block block);
	}
}

