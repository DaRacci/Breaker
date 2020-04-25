package com.asangarin.breaker.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.api.BreakState;
import com.asangarin.breaker.api.BreakTrigger;
import com.asangarin.breaker.api.TriggerType;
import com.asangarin.breaker.core.BlockConfiguration;

public class ConfigManager {
	private List<BreakState> states = new ArrayList<>();
	private List<BreakTrigger> triggers = new ArrayList<>();
	
	public ConfigManager() {
		for(Class<? extends BreakState> breakStates : Breaker.plugin.states.registered())
			try { states.add(breakStates.newInstance()); } catch (InstantiationException | IllegalAccessException e) { e.printStackTrace(); }
		for(Class<? extends BreakTrigger> breakTriggers : Breaker.plugin.triggers.registered())
			try { triggers.add(breakTriggers.newInstance()); } catch (InstantiationException | IllegalAccessException e) { e.printStackTrace(); }
	}
	
	public void reload() {
		Breaker.plugin.database.clear();
		
		//Read all files in the blockconfigs folder
	    for (File file : new File(Breaker.plugin.getDataFolder(), "blockconfigs").listFiles()) {
	        if (!file.isDirectory() && file.getName().substring(file.getName().lastIndexOf('.')).equalsIgnoreCase(".yml")) {
	        	YamlConfiguration c = YamlConfiguration.loadConfiguration(file);
	        	
	        	c.getKeys(false).forEach(entries -> {
	    			ConfigurationSection config = c.getConfigurationSection(entries);
	    			BlockConfiguration block;
	    			
	    			try {
	    				block = new BlockConfiguration(config.getName().replace('-', '_'),
	    					config.getInt("min-hardness", 1), config.getInt("max-hardness", 4));
	    			}
	    			catch(Exception ex) {
	    				Breaker.warn("Couldn't load Material: " + config.getName());
	    				return;
	    			}
	    			
	    			if(config.contains("states")) config.getConfigurationSection("states").getKeys(false).forEach(cstate -> {
	    				BreakState breakState = null;
	    				String type = config.getString("states." + cstate + ".type");
	    				if(type == null) {
	    					Breaker.warn("You haven't specified a type for '" + cstate + "' (state)!");
	    					return;
	    				}

	                    for(BreakState state : states) {
	                    	if(type.equalsIgnoreCase(state.type()))
	                    	{ breakState = state; break; }
	                    }
	    				
	    				if(breakState != null) {
	    					Breaker.debug("Added '" + type + "' state to: '" + block.getId() + "'", 2);
	    					try {
								block.addState(breakState.getClass().newInstance().register(config.getConfigurationSection("states." + cstate)));
							} catch (InstantiationException | IllegalAccessException e) {
								e.printStackTrace();
							}
	    				}
	    				else
	    					Breaker.warn("Couldn't find BreakState: " + type);
	    			});
	    			if(config.contains("triggers")) for(TriggerType types : TriggerType.values()) {
	    				if(config.contains("triggers.on-" + types.name().toLowerCase())) config.getConfigurationSection("triggers.on-" + types.name().toLowerCase()).getKeys(false).forEach(ctrigger -> {
		    				BreakTrigger breakTrigger = null;
		    				String type = config.getString("triggers.on-" + types.name().toLowerCase() + "." + ctrigger + ".type");
		    				if(type == null) {
		    					Breaker.warn("You haven't specified a type for '" + ctrigger + "' (trigger)!");
		    					return;
		    				}

		                    for(BreakTrigger trigger : triggers) {
		                    	if(type.equalsIgnoreCase(trigger.type()))
		                    	{ breakTrigger = trigger; break; }
		                    }
		                    
		    				if(breakTrigger != null) {
		    					Breaker.debug("Added '" + type + "' (on-" + types.name().toLowerCase() + ") trigger to: '" + block.getId() + "'", 2);
		    					try {
									block.addTrigger(types, breakTrigger.getClass().newInstance().register(config.getConfigurationSection("triggers.on-" + types.name().toLowerCase() + "." + ctrigger)));
								} catch (InstantiationException | IllegalAccessException e) {
									e.printStackTrace();
								}
		    				}
		    				else
		    					Breaker.warn("Couldn't find BreakTrigger: " + type);
	    			});
    				}
	    			
	    			Breaker.plugin.database.add(block.getId(), block);
	    		});
	        }
	    }
	}
}
