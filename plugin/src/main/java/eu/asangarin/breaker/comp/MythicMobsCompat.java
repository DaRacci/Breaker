package eu.asangarin.breaker.comp;

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
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Optional;

public class MythicMobsCompat {
	public static void castSkill(Player player, String skill) {
		Optional<Skill> optionalSkill = MythicMobs.inst().getSkillManager().getSkill(skill);
		optionalSkill.ifPresentOrElse((s) -> {
			AbstractEntity trigger = BukkitAdapter.adapt(player);
			SkillCaster caster = new GenericCaster(trigger);
			AbstractLocation loc = BukkitAdapter.adapt(player.getEyeLocation());

			HashSet<AbstractEntity> targetEntities = new HashSet<>();
			targetEntities.add(trigger);
			HashSet<AbstractLocation> targetLocations = new HashSet<>();
			targetLocations.add(loc);

			SkillMetadata meta = new SkillMetadata(SkillTrigger.API, caster, trigger, loc, targetEntities, targetLocations, 1);
			if (s.isUsable(meta)) s.execute(meta);
		}, () -> Breaker.error("Tried running a MythicMobs skill, but '" + skill + "' is not a valid skill!"));
	}
}
