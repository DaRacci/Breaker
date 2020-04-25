package com.asangarin.breaker.comp.mmocore;

import org.bukkit.configuration.ConfigurationSection;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.api.BreakState;
import com.asangarin.breaker.core.BreakingBlock;

import net.Indyuce.mmocore.api.player.PlayerData;

public class ClassState implements BreakState {
	private int value;
	private String profess;
	
	public ClassState register(String c, int val) {
		this.profess = c;
		this.value = val;
		return this;
	}
	
	@Override
	public String type() {
		return "class";
	}
	
	@Override
	public boolean activeState(BreakingBlock block) {
		PlayerData data = PlayerData.get(block.getBreaker());
		Breaker.debug("[MMOCore] Class Test: " + profess + " | " + data.getProfess().getId(), 6);
		if(data.getProfess().getId().equalsIgnoreCase(profess))
			return true;
		return false;
	}

	@Override
	public int getStateValue(BreakingBlock block) {
		return value;
	}
	
	@Override
	public BreakState register(ConfigurationSection c) {
		return register(c.getString("class", "human"), c.getInt("hardness", 1));
	}
}
