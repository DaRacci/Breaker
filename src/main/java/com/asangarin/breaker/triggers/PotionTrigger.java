package com.asangarin.breaker.triggers;

import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.utility.BreakTrigger;

public class PotionTrigger implements BreakTrigger {
	PotionEffectType potion;
	private int amp, dur;
	
	PotionTrigger register(PotionEffectType pet, int d, int a) {
		potion = pet;
		amp = a;
		dur = d;
		return this;
	}
	
	@Override
	public String type() {
		return "potion";
	}
	
	@Override
	public void execute(Player player, Block block) {
		player.addPotionEffect(new PotionEffect(potion, dur, amp), true);
	}

	@Override
	public BreakTrigger register(ConfigurationSection c) {
		try {
			return register(PotionEffectType.getByName(c.getString("effect", "SPEED")), c.getInt("duration", 600), c.getInt("amplifier", 1));
		} catch(Exception e) {
			Breaker.warn("'" + c.getName() + "' couldn't read potion effect of '" + c.getString("effect") + "'!");
			return null;
		}
	}
}
