package com.asangarin.breaker;

import org.bukkit.configuration.ConfigurationSection;

//TODO: add a few more settings
public class Settings {
	private final boolean permaMF, mappedReflection;
	
	public Settings(ConfigurationSection config) {
		permaMF = config.getBoolean("permanent-mining-fatigue");
		mappedReflection = config.getBoolean("advanced.use-mapped-reflection");
	}
	
	public static Settings instance()
	{ return Breaker.plugin.settings; }
	
	public boolean permanentMiningFatigue()
	{ return permaMF; }

	public boolean useMappedReflection()
	{ return mappedReflection; }
}
