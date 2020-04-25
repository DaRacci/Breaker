package com.asangarin.breaker.comp.mmoitems;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import com.asangarin.breaker.api.BreakState;
import com.asangarin.breaker.core.BreakingBlock;

import net.mmogroup.mmolib.api.item.NBTItem;

public class MMOItemState implements BreakState {
	private int requiredstat, value;
	private String type, id;
	private String statName;
	
	public MMOItemState register(String stat, int statValue, String type, String id, int val) {
		this.statName = stat;
		this.requiredstat = statValue;
		this.type = type;
		this.id = id;
		this.value = val;
		return this;
	}
	
	@Override
	public String type() {
		return "mmoitem";
	}
	
	@Override
	public boolean activeState(BreakingBlock block) {
		ItemStack stack = block.getBreaker().getInventory().getItemInMainHand();
		if(stack.getType() != Material.AIR) {
			NBTItem nbt = NBTItem.get(stack);
			
			if(nbt.hasType())
				if(type.equals("any") || type.equals(nbt.getType().getName().toLowerCase()))
					if(id.equals("any") || id.equals(nbt.getString("MMOITEMS_ITEM_ID").toLowerCase()))
						if(statName.equals("NONE") || requiredstat <= nbt.getInteger("MMOITEMS_" + statName))
							return true;
		}
		
		return false;
	}

	@Override
	public int getStateValue(BreakingBlock block) {
		return value;
	}
	
	@Override
	public BreakState register(ConfigurationSection c) {
		int v = c.getInt("hardness", 1);
		String t = c.getString("item_type", "any").toLowerCase();
		String i = c.getString("item_id", "any").toLowerCase();
		String name = c.getString("stat_name", "none").toUpperCase();
		
		int req = c.getInt("stat_value", 0);
		
		return register(name, req, t, i, v);
	}
}
