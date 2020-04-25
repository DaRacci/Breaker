package com.asangarin.breaker.states;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.api.BreakState;
import com.asangarin.breaker.core.BreakingBlock;

public class ToolState implements BreakState {
	private Material material;
	private int value;

	public ToolState register(Material mat, int val) {
		material = mat;
		value = val;
		return this;
	}

	@Override
	public String type() {
		return "tool";
	}
	
	@Override
	public boolean activeState(BreakingBlock block) {
		Breaker.debug("Tool Test: " + block.getBreaker().getInventory().getItemInMainHand().getType() + " | " + material, 6);
		
		if(block.getBreaker().getInventory().getItemInMainHand().getType() == material)
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
			return register(Material.valueOf(c.getString("material", "AIR")), c.getInt("hardness", 1));
		} catch(Exception e) {
			Breaker.warn("'" + c.getName() + "' couldn't read material of '" + c.getString("material") + "'!");
			return null;
		}
	}
}
