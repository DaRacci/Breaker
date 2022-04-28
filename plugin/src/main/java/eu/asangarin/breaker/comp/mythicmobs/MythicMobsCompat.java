package eu.asangarin.breaker.comp.mythicmobs;

import eu.asangarin.breaker.Breaker;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.mobs.GenericCaster;
import io.lumine.mythic.api.skills.Skill;
import io.lumine.mythic.api.skills.SkillCaster;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.skills.SkillMetadataImpl;
import io.lumine.mythic.core.skills.SkillTriggers;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Optional;

public class MythicMobsCompat {
	public static void castSkill(Player player, Location location, String skill) {
		Optional<Skill> optionalSkill = MythicBukkit.inst().getSkillManager().getSkill(skill);
		if (optionalSkill.isPresent()) {
			Skill s = optionalSkill.get();
			SkillMetadata meta = createMeta(player, location.add(0.5, 0.5, 0.5));
			if (s.isUsable(meta)) s.execute(meta);
		} else {
			Breaker.error("Tried running a MythicMobs skill, but '" + skill + "' is not a valid skill!");
		}
	}

	public static SkillMetadata createMeta(Player player, Location location) {
		AbstractEntity trigger = BukkitAdapter.adapt(player);
		SkillCaster caster = new GenericCaster(trigger);
		AbstractLocation loc = BukkitAdapter.adapt(location);

		HashSet<AbstractEntity> targetEntities = new HashSet<>();
		targetEntities.add(trigger);
		HashSet<AbstractLocation> targetLocations = new HashSet<>();
		targetLocations.add(loc);

		return new SkillMetadataImpl(SkillTriggers.API, caster, trigger, loc, targetEntities, targetLocations, 1);
	}
}
