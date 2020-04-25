package com.asangarin.breaker.triggers;

import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.asangarin.breaker.api.BreakTrigger;

public class CommandTrigger implements BreakTrigger {
	String command;
	
	CommandTrigger register(String cmd) {
		command = cmd;
		return this;
	}
	
	@Override
	public String type() {
		return "command";
	}
	
	@Override
	public void execute(Player player, Block block) {
		player.performCommand(command);
	}

	@Override
	public BreakTrigger register(ConfigurationSection c) {
		return register(c.getString("command", "say no command!"));
	}
}
