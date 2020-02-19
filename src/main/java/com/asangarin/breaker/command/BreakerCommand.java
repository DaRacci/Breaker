package com.asangarin.breaker.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.asangarin.breaker.Breaker;

public class BreakerCommand implements CommandExecutor
{
	String pluginPrefix = ChatColor.translateAlternateColorCodes('&', "&6[&4Breaker&6] ");
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;

			if(!player.hasPermission("breaker.admin")) {
				player.sendMessage(ChatColor.RED + "You do not have permission to run this command.");
				return true;
			}
		}
		
		if(args == null || args.length == 0)
			sender.sendMessage(pluginPrefix + ChatColor.GREEN + "Version " + Breaker.plugin.getDescription().getVersion());
		else {
			switch(args[0]) {
				case "reload":
					Breaker.plugin.onReload();
					sender.sendMessage(pluginPrefix + ChatColor.GREEN + "Successfully reloaded.");
					break;
				default:
					sender.sendMessage(pluginPrefix + ChatColor.RED + "Unknown command.");
					break;
			}
		}
		return true;
	}
}
