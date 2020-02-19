package com.asangarin.breaker.comp.worldguard;

import org.bukkit.configuration.ConfigurationSection;

import com.asangarin.breaker.core.BreakingBlock;
import com.asangarin.breaker.utility.BreakState;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionQuery;

public class RegionState implements BreakState {
	private int value;
	private String region;
	
	public RegionState register(String r, int val) {
		this.region = r;
		this.value = val;
		return this;
	}

	@Override
	public String type() {
		return "region";
	}
	
	@Override
	public boolean activeState(BreakingBlock block) {
		RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
		ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(block.getBlock().getLocation()));
		for(ProtectedRegion r : set.getRegions())
			if(r.getId().equalsIgnoreCase(region))
				return true;
		return false;
	}

	@Override
	public int getStateValue(BreakingBlock block) {
		return value;
	}

	@Override
	public BreakState register(ConfigurationSection c) {
		return register(c.getString("world", "world").toLowerCase(), c.getInt("hardness", 1));
	}
}
