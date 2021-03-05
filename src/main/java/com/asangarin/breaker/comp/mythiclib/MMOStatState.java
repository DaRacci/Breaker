package com.asangarin.breaker.comp.mythiclib;

import com.asangarin.breaker.api.BreakState;
import com.asangarin.breaker.core.BreakingBlock;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import org.bukkit.configuration.ConfigurationSection;

public class MMOStatState implements BreakState {
	private double required;
	private int value;
	private String statName;

	public MMOStatState register(String stat, double statValue, int val) {
		this.statName = stat;
		this.required = statValue;
		this.value = val;
		return this;
	}

	@Override
	public String type() {
		return "mmostat";
	}

	@Override
	public boolean activeState(BreakingBlock block) {
		MMOPlayerData playerData = MMOPlayerData.get(block.getBreaker());
		return playerData.getStatMap().getStat(statName) >= required;
	}

	@Override
	public int getStateValue(BreakingBlock block) {
		return value;
	}

	@Override
	public BreakState register(ConfigurationSection c) {
		int v = c.getInt("hardness", 1);
		String name = c.getString("stat_name", "none");
		double req = c.getDouble("stat_value", 0.0);

		return register(name, req, v);
	}
}
