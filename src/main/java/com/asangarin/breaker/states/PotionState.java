package com.asangarin.breaker.states;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.potion.PotionEffectType;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.api.BreakState;
import com.asangarin.breaker.core.BreakingBlock;

public class PotionState implements BreakState {
	private PotionEffectType effect;
	private int min, value;

	public PotionState register(PotionEffectType e, int m, int val) {
		effect = e;
		min = m;
		value = val;
		return this;
	}

	@Override
	public String type() {
		return "potion";
	}
	
	@Override
	public boolean activeState(BreakingBlock block) {
		Breaker.debug("Potion Test: " + block.getBreaker().getPotionEffect(effect) + " | " + effect, 6);
		
		if(block.getBreaker().getPotionEffect(effect) != null &&
			block.getBreaker().getPotionEffect(effect).getAmplifier() >= min)
			return true;
		return false;
	}

	@Override
	public int getStateValue(BreakingBlock block) {
		return value;
	}

	@Override
	public BreakState register(ConfigurationSection c) {
		try {
			return register(PotionEffectType.getByName(c.getString("effect", "SPEED")), c.getInt("amplifier", 0), c.getInt("hardness", 1));
		} catch(Exception e) {
			Breaker.warn("'" + c.getName() + "' couldn't read potion effect of '" + c.getString("type") + "'!");
			return null;
		}
	}
}
