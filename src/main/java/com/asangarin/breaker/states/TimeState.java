package com.asangarin.breaker.states;

import org.bukkit.configuration.ConfigurationSection;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.api.BreakState;
import com.asangarin.breaker.core.BreakingBlock;

public class TimeState implements BreakState {
	private long minTime;
	private long maxTime;
	private int value;

	public TimeState register(long minTime, long maxTime, int val) {
		this.minTime = minTime;
		this.maxTime = maxTime;
		this.value = val;
		return this;
	}

	@Override
	public String type() {
		return "time";
	}
	
	@Override
	public boolean activeState(BreakingBlock block) {
		Breaker.debug("Time Test: " + block.getBlock().getWorld().getTime() + " | (max) " + maxTime + " (min) " + minTime, 6);
		
		if(block.getBlock().getWorld().getTime() > minTime
			&& block.getBlock().getWorld().getTime() < maxTime)
				return true;
		
		return false;
	}

	@Override
	public int getStateValue(BreakingBlock block) {
		return value;
	}

	@Override
	public BreakState register(ConfigurationSection c) {
		long min = convertTime(c.getString("min_time", ""));
		long max = convertTime(c.getString("max_time", ""));
		
		if((min > 0 && min < 24001) ||
			(max > 0 && max < 24001))
				return register(min, max, c.getInt("hardness", 1));
		
		Breaker.warn("'" + c.getName() + "' doesn't have a correct time value!");
		return null;
	}

	private long convertTime(String time) {
		if(time.matches("\\d+"))
			return Math.min(24000, Math.max(0, Long.parseLong(time)));
		
		if(time.equalsIgnoreCase("day")) return 1000;
		if(time.equalsIgnoreCase("noon")) return 6000;
		if(time.equalsIgnoreCase("sunset")) return 12000;
		if(time.equalsIgnoreCase("night")) return 13000;
		if(time.equalsIgnoreCase("midnight")) return 18000;
		if(time.equalsIgnoreCase("sunrise")) return 23000;
		return -1;
	}
}
