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
	private final HashMap<String, Consumer<CommandSender>> commands = new HashMap<>();

	public BreakerCommand() {
		addCommand("reload", (s) -> {
			Breaker.get().reload();
			s.sendMessage(pluginPrefix + "Plugin successfully reloaded!");
			if (s instanceof Player) Breaker.log("Breaker successfully reloaded.");
		});
		addCommand("editor", (s) -> {
			if (!(s instanceof Player)) {
				s.sendMessage(pluginPrefix + ChatColor.RED + "This command can only be run by players.");
				return;
			}
			s.sendMessage(pluginPrefix + "This feature is not yet implemented!");
		});
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String commandLabel, String[] args) {
		if (args.length < 1) return true;

		commands.getOrDefault(args[0], (s) -> s.sendMessage(pluginPrefix + ChatColor.RED + "Unknown subcommand")).accept(sender);
		return true;
	}

	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
		final List<String> completions = new ArrayList<>();
		StringUtil.copyPartialMatches(args[0], commands.keySet(), completions);
		Collections.sort(completions);
		return completions;
	}

	private void addCommand(String subcommand, Consumer<CommandSender> function) {
		commands.put(subcommand, function);
	}
}
