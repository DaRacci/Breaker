package com.asangarin.breaker.triggers;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.api.BreakTrigger;

import me.clip.placeholderapi.PlaceholderAPI;

public class CommandTrigger implements BreakTrigger {
	private String command;
	private boolean console;
	
	CommandTrigger register(String cmd, boolean cs) {
		command = cmd;
		console = cs;
		return this;
	}
	
	@Override
	public String type() {
		return "command";
	}
	
	@Override
	public void execute(Player player, Block block) {
		if(console) Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), parsedCommand(player, command));
		player.performCommand(parsedCommand(player, command));
	}

	@Override
	public BreakTrigger register(ConfigurationSection c) {
		return register(c.getString("command", "say no command!"), c.getBoolean("console", false));
	}
	
	public String parsedCommand(Player player, String cmd) {
		if(!Breaker.plugin.isLoaded("PlaceholderAPI")) return cmd;
		return PlaceholderAPI.setPlaceholders(player, cmd);
	}
}
