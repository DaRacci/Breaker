package eu.asangarin.breaker.cmd;

import eu.asangarin.breaker.Breaker;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class BreakerCommand implements CommandExecutor, TabCompleter {
	private final String pluginPrefix = ChatColor.GOLD + "[" + ChatColor.GREEN + Breaker.get().getName() + ChatColor.GOLD + "] " + ChatColor.AQUA;
	private final HashMap<String, Consumer<Player>> commands = new HashMap<>();

	public BreakerCommand() {
		addCommand("reload", (p) -> {
			Breaker.get().reload();
			p.sendMessage(pluginPrefix + "Plugin successfully reloaded!");
			Breaker.log("Reloaded.");
		});
		addCommand("editor", (p) -> p.sendMessage(pluginPrefix + "This feature is not yet implemented!"));
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String commandLabel, String[] args) {
		if (args.length < 1 || !(sender instanceof Player)) return true;

		commands.getOrDefault(args[0], (p) -> p.sendMessage(pluginPrefix + ChatColor.RED + "Unknown command")).accept((Player) sender);
		return true;
	}

	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
		final List<String> completions = new ArrayList<>();
		StringUtil.copyPartialMatches(args[0], commands.keySet(), completions);
		Collections.sort(completions);
		return completions;
	}

	private void addCommand(String subcommand, Consumer<Player> function) {
		commands.put(subcommand, function);
	}
}
