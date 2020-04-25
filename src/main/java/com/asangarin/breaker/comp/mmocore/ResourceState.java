package com.asangarin.breaker.comp.mmocore;

import org.bukkit.configuration.ConfigurationSection;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.api.BreakState;
import com.asangarin.breaker.core.BreakingBlock;

import net.Indyuce.mmocore.api.player.PlayerData;

public class ResourceState implements BreakState {
	private int value;
	private double amount;
	private String resource;
	
	public ResourceState register(String res, double a, int val) {
		this.resource = res;
		this.amount = a;
		this.value = val;
		return this;
	}
	
	@Override
	public String type() {
		return "resource";
	}
	
	@Override
	public boolean activeState(BreakingBlock block) {
		PlayerData data = PlayerData.get(block.getBreaker());
		Breaker.debug("[MMOCore] Resource Test: " + amount + " | " + data.getStellium() + " - " + data.getMana() + " - " + data.getStamina(), 6);
		if((resource.equalsIgnoreCase("mana") && data.getMana() >= amount)
			|| (resource.equalsIgnoreCase("stamina") && data.getStamina() >= amount)
			|| (resource.equalsIgnoreCase("stellium") && data.getStellium() >= amount))
			return true;
		return false;
	}

	@Override
	public int getStateValue(BreakingBlock block) {
		return value;
	}
	
	@Override
	public BreakState register(ConfigurationSection c) {
		String resource = c.getString("resource", "mana");
		if(resource.equalsIgnoreCase("mana")
			|| resource.equalsIgnoreCase("stamina")
			|| resource.equalsIgnoreCase("stellium"))
			return register(resource, c.getDouble("amount", 1), c.getInt("hardness", 1));
		else {
			Breaker.warn("'" + c.getName() + "' - '" + resource + "' is not a valid resource!");
			return null;
		}
	}
}
