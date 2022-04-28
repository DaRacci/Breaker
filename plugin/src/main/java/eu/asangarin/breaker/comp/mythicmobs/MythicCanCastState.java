package eu.asangarin.breaker.comp.mythicmobs;

import eu.asangarin.breaker.api.BreakerState;
import io.lumine.mythic.api.skills.Skill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.utils.config.LineConfig;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Optional;

public class MythicCanCastState extends BreakerState {
	private Skill skill;

	@Override
	public boolean isConditionMet(Player breaker, Block block) {
		SkillMetadata meta = MythicMobsCompat.createMeta(breaker, block.getLocation());
		return skill.isUsable(meta);
	}

	@Override
	protected boolean setup(LineConfig config) {
		String s = config.getString("skill");
		if (s == null) return error("'mmcast' is missing the key arg!");
		Optional<Skill> optionalSkill = MythicBukkit.inst().getSkillManager().getSkill(s);
		if (optionalSkill.isPresent()) skill = optionalSkill.get();
		else return error("'mmcast' state couldn't add skill. '" + skill + "' is not valid!");
		return true;
	}
}