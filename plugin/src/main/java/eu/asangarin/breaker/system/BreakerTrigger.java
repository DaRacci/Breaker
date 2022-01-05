package eu.asangarin.breaker.system;

import eu.asangarin.breaker.Breaker;
import eu.asangarin.breaker.api.BreakerTriggerEvent;
import eu.asangarin.breaker.util.TriggerFunction;
import eu.asangarin.breaker.util.TriggerType;
import io.lumine.mythic.utils.config.LineConfig;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.GenericCaster;
import io.lumine.xikage.mythicmobs.skills.Skill;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
		return error(name, "Your '" + config.getKey() + "' does not have a valid function!");
	}

	private boolean error(String name, String arg) {
		Breaker.error("[" + name + "] Couldn't add trigger: " + arg);
		return false;
	}

	public TriggerTrigger prepare() {
		return switch (function) {
			case COMMAND -> (player, block) -> {
				String cmd = args.replace("%player%", player.getName());
				if (cmd.startsWith("!")) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.substring(1));
				else Bukkit.dispatchCommand(player, cmd);
			};
			case SKILL -> (player, block) -> {
				if (!Breaker.get().isMmSupport()) {
					Breaker.error("Tried adding a MythicMobs skill to a trigger, but MythicMobs isn't enabled!");
					return;
				}
				Optional<Skill> skill = MythicMobs.inst().getSkillManager().getSkill(args);
				skill.ifPresentOrElse((s) -> {
					AbstractEntity trigger = BukkitAdapter.adapt(player);
					SkillCaster caster = new GenericCaster(trigger);
					AbstractLocation loc = BukkitAdapter.adapt(player.getEyeLocation());

					HashSet<AbstractEntity> targetEntities = new HashSet<>();
					targetEntities.add(trigger);
					HashSet<AbstractLocation> targetLocations = new HashSet<>();
					targetLocations.add(loc);

					SkillMetadata meta = new SkillMetadata(SkillTrigger.API, caster, trigger, loc, targetEntities, targetLocations, 1);
					if (s.isUsable(meta)) s.execute(meta);
				}, () -> Breaker.error("Tried running a MythicMobs skill, but '" + args + "' is not a valid skill!"));
			};
			case EVENT -> (player, block) -> Bukkit.getPluginManager().callEvent(new BreakerTriggerEvent(player, block, args));
		};
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

