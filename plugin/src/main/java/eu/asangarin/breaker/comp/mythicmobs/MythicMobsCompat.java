package eu.asangarin.breaker.comp.mythicmobs;

import eu.asangarin.breaker.Breaker;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.GenericCaster;
import io.lumine.xikage.mythicmobs.skills.Skill;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Optional;

public class MythicMobsCompat {
	public static void castSkill(Player player, Location location, String skill) {
		Optional<Skill> optionalSkill = MythicMobs.inst().getSkillManager().getSkill(skill);
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

		return new SkillMetadata(SkillTrigger.API, caster, trigger, loc, targetEntities, targetLocations, 1);
	}
}
