package com.asangarin.breaker.states;

import org.bukkit.configuration.ConfigurationSection;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.api.BreakState;
import com.asangarin.breaker.core.BreakingBlock;

public class ExperienceState implements BreakState {
	private int value, level, experience;

	public ExperienceState register(int l, int exp, int val) {
		this.value = val;
		this.level = l;
		this.experience = exp;
		return this;
	}

	@Override
	public String type() {
		return "experience";
	}
	
	@Override
	public boolean activeState(BreakingBlock block) {
		Breaker.debug("Experience Test: " + block.getBreaker().getLevel() + " - " + block.getBreaker().getTotalExperience() + " | (level) " + level + " (exp) " + experience, 6);
		
		if(block.getBreaker().getLevel() >= level &&
			block.getBreaker().getTotalExperience() >= experience)
				return true;
		
		return false;
	}

	@Override
	public int getStateValue(BreakingBlock block) {
		return value;
	}

	@Override
	public BreakState register(ConfigurationSection c) {
		return register(c.getInt("experience", 0), c.getInt("level", 0), c.getInt("hardness", 1));
	}
}
