package com.asangarin.breaker.triggers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
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
		if (console)
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
					parsedCommand(player, block.getLocation(), command));
		else player.performCommand(parsedCommand(player, block.getLocation(), command));
	}

	@Override
	public BreakTrigger register(ConfigurationSection c) {
		return register(c.getString("command", "say no command!"), c.getBoolean("console", false));
	}

	public String parsedCommand(Player player, Location loc, String cmd) {
		cmd = cmd.replace("{player}", player.getName())
				.replace("{blockx}", "" + loc.getX())
				.replace("{blocky}", "" + loc.getY())
				.replace("{blockz}", "" + loc.getZ())
				.replace("{playerx}", "" + player.getLocation().getX())
				.replace("{playery}", "" + player.getLocation().getY())
				.replace("{playerz}", "" + player.getLocation().getZ())
				.replace("{blockxint}", "" + loc.getBlockX())
				.replace("{blockyint}", "" + loc.getBlockY())
				.replace("{blockzint}", "" + loc.getBlockZ())
				.replace("{playerxint}", "" + player.getLocation().getBlockX())
				.replace("{playeryint}", "" + player.getLocation().getBlockY())
				.replace("{playerzint}", "" + player.getLocation().getBlockZ());
		if (!Breaker.plugin.isLoaded("PlaceholderAPI"))
			return cmd;
		return PlaceholderAPI.setPlaceholders(player, cmd);
	}
}
