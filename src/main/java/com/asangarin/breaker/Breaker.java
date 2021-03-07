package com.asangarin.breaker;

import com.asangarin.packkit.Packkit;
import io.lumine.mythic.utils.plugin.LuminePlugin;
import lombok.Getter;

@Getter
public class Breaker extends LuminePlugin {
	private static Breaker plugin;
	private final Packkit packkit = new Packkit(new BreakerNetworkHandler());

	private final SystemManager systemManager = new SystemManager();

	@Override
	public void enable() {
		plugin = this;
		getServer().getPluginManager().registerEvents(packkit, this);
	}

	@Override
	public void disable() {
		plugin = null;
	}

	public static Breaker get() {
		return plugin;
	}
}
