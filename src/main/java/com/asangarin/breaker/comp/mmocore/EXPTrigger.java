package com.asangarin.breaker.comp.mmocore;

import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.asangarin.breaker.utility.BreakTrigger;

import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.player.PlayerData;

public class EXPTrigger implements BreakTrigger {
	String profess;
	int experience;
	
	EXPTrigger register(String pro, int exp) {
		profess = pro;
		experience = exp;
		return this;
	}
	
	@Override
	public String type() {
		return "giveexp";
	}
	
	@Override
	public void execute(Player player, Block block) {
		PlayerData data = PlayerData.get(player);
		if(profess.equalsIgnoreCase("main")) data.giveExperience(experience, block.getLocation());
		else data.getCollectionSkills().giveExperience(MMOCore.plugin.professionManager.get(profess), experience, block.getLocation());
	}

	@Override
	public BreakTrigger register(ConfigurationSection c) {
		return register(c.getString("profession", "main"), c.getInt("experience", 1));
	}
}
