package com.asangarin.breaker;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Breaker extends JavaPlugin {
	private final SystemManager systemManager = new SystemManager();
	private static Breaker plugin;

	@Override
	public void onEnable() {
		plugin = this;
	}

	@Override
	public void onDisable() {
		plugin = null;
	}

	public static Breaker get() {
		return plugin;
	}
}
