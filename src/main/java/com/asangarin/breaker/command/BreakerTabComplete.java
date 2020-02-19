package com.asangarin.breaker.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

public class BreakerTabComplete implements TabCompleter
{
	private static final List<String> FIRST_ARGS = Arrays.asList("reload");
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
        final List<String> completions = new ArrayList<>();

		if(args == null || args.length == 0)
	        StringUtil.copyPartialMatches(args[0], FIRST_ARGS, completions);
		else
		{
			switch(args.length)
			{
				default:
					completions.clear();
					break;
				case 1:
			        StringUtil.copyPartialMatches(args[0], FIRST_ARGS, completions);
			        break;
			}
		}

        Collections.sort(completions);
        return completions;
    }
}
