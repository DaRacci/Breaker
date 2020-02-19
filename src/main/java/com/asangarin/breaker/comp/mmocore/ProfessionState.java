package com.asangarin.breaker.comp.mmocore;

import org.bukkit.configuration.ConfigurationSection;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.core.BreakingBlock;
import com.asangarin.breaker.utility.BreakState;

import net.Indyuce.mmocore.api.player.PlayerData;

public class ProfessionState implements BreakState {
	private int level, value;
	private String profess;
	
	public ProfessionState register(String pro, int l, int val) {
		this.profess = pro;
		this.level = l;
		this.value = val;
		return this;
	}
	
	@Override
	public String type() {
		return "profession";
	}
	
	@Override
	public boolean activeState(BreakingBlock block) {
		PlayerData data = PlayerData.get(block.getBreaker());
		Breaker.debug("[MMOCore] Profession Test: " + profess + " - " + level + " | " + data.getLevel() + " - " + data.getCollectionSkills().getLevel(profess), 6);
		if((profess.equalsIgnoreCase("main") && data.getLevel() >= level)
			|| data.getCollectionSkills().getLevel(profess) >= level)
			return true;
		return false;
	}

	@Override
	public int getStateValue(BreakingBlock block) {
		return value;
	}
	
	@Override
	public BreakState register(ConfigurationSection c) {
		return register(c.getString("profession", "main"), c.getInt("level", 1), c.getInt("hardness", 1));
	}
}
