package com.asangarin.breaker;

import org.bukkit.configuration.ConfigurationSection;

//TODO: add a few more settings
public class Settings {
	private boolean permaMF;
	
	public Settings(ConfigurationSection config) {
		permaMF = config.getBoolean("permanent-mining-fatigue");
	}
	
	public static Settings instance()
	{ return Breaker.plugin.settings; }
	
	public boolean permanentMiningFatigue()
	{ return permaMF; }
}
