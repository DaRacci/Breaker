package com.asangarin.breaker.states;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.api.BreakState;
import com.asangarin.breaker.core.BreakingBlock;

public class EnchantmentState implements BreakState {
	private Enchantment ench;
	private int min, value;

	public EnchantmentState register(Enchantment e, int m, int val) {
		ench = e;
		min = m;
		value = val;
		return this;
	}

	@Override
	public String type() {
		return "enchant";
	}
	
	@Override
	public boolean activeState(BreakingBlock block) {
		Breaker.debug("Enchantment Test: " + block.getBreaker().getInventory().getItemInMainHand().containsEnchantment(ench) + " | " + ench, 6);
		
		ItemStack stack = block.getBreaker().getInventory().getItemInMainHand();
		if(stack != null && stack.getType() != Material.AIR &&
			stack.containsEnchantment(ench) && stack.getEnchantmentLevel(ench) >= min)
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
			return register(Enchantment.getByKey(NamespacedKey.minecraft(c.getString("enchantment", "sharpness").toLowerCase())), c.getInt("level", 1), c.getInt("hardness", 1));
		} catch(Exception e) {
			Breaker.warn("'" + c.getName() + "' couldn't read enchantment of '" + c.getString("type") + "'!");
			return null;
		}
	}
}
