package com.asangarin.breaker.comp.mmocore;

import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.utility.BreakTrigger;

import net.Indyuce.mmocore.api.player.PlayerData;

public class RestoreTrigger implements BreakTrigger {
	String resource;
	double amount;
	
	RestoreTrigger register(String res, double a) {
		resource = res;
		amount = a;
		return this;
	}
	
	@Override
	public String type() {
		return "restore";
	}
	
	@Override
	public void execute(Player player, Block block) {
		PlayerData data = PlayerData.get(player);
		if(resource.equalsIgnoreCase("health")) data.heal(amount);
		if(resource.equalsIgnoreCase("mana")) data.giveMana(amount);
		if(resource.equalsIgnoreCase("stamina")) data.giveStamina(amount);
		if(resource.equalsIgnoreCase("stellium")) data.giveStellium(amount);
	}

	@Override
	public BreakTrigger register(ConfigurationSection c) {

		String resource = c.getString("resource", "mana");
		if(resource.equalsIgnoreCase("mana")
			|| resource.equalsIgnoreCase("stamina")
			|| resource.equalsIgnoreCase("stellium")
			|| resource.equalsIgnoreCase("health"))
			return register(resource, c.getDouble("amount", 1));
		else {
			Breaker.warn("'" + c.getName() + "' - '" + resource + "' is not a valid resource!");
			return null;
		}
	}
}
