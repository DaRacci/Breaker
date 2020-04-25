package com.asangarin.breaker.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.asangarin.breaker.api.BreakState;
import com.asangarin.breaker.api.BreakTrigger;
import com.asangarin.breaker.api.TriggerType;

/**
 * The configuration of a block.
 * These properties are documented on the wiki.
 */
public class BlockConfiguration {
	private final String id;
	private final int minHardness, maxHardness;
	private List<BreakState> states = new ArrayList<>();
	private Map<TriggerType, List<BreakTrigger>> triggers = new HashMap<>();
	
	public BlockConfiguration(String i, int minH, int maxH) { 
		for(TriggerType type : TriggerType.values())
			triggers.put(type, new ArrayList<>());
			
		id = i; minHardness = minH; maxHardness = maxH;
	}

	public void addState(BreakState state)
	{ states.add(state); }
	public List<BreakState> getStates()
	{ return states; }
	public void addTrigger(TriggerType type, BreakTrigger trigger)
	{ triggers.get(type).add(trigger); }
	public List<BreakTrigger> getTriggers(TriggerType type)
	{ return triggers.get(type); }
	public String getId()
	{ return id; }
	public int getMinHardness()
	{ return minHardness; }
	public int getMaxHardness()
	{ return maxHardness; }
}
