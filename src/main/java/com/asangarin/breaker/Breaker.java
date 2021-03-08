package com.asangarin.breaker;

import com.asangarin.breaker.network.BreakerNetworkHandler;
import com.asangarin.packkit.Packkit;
import io.lumine.mythic.utils.plugin.LuminePlugin;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Getter
public class Breaker extends LuminePlugin {
	private static Breaker plugin;
	private final Packkit packkit = new Packkit(new BreakerNetworkHandler());
	private final SystemManager systemManager = new SystemManager();

	@Override
	public void enable() {
		plugin = this;
		getServer().getPluginManager().registerEvents(packkit, this);
		for(Player player : Bukkit.getServer().getOnlinePlayers())
			packkit.inject(player);
	}

	@Override
	public void disable() {
		plugin = null;

		for (Player player : getServer().getOnlinePlayers())
			packkit.close(player);
	}

	public static Breaker get() {
		return plugin;
	}
}
