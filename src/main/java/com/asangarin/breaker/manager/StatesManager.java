package com.asangarin.breaker.manager;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.comp.mmocore.ClassState;
import com.asangarin.breaker.comp.mmocore.ProfessionState;
import com.asangarin.breaker.comp.mmocore.ResourceState;
import com.asangarin.breaker.comp.mmoitems.MMOItemState;
import com.asangarin.breaker.comp.worldguard.RegionState;
import com.asangarin.breaker.states.EnchantmentState;
import com.asangarin.breaker.states.ExperienceState;
import com.asangarin.breaker.states.FlyingState;
import com.asangarin.breaker.states.PotionState;
import com.asangarin.breaker.states.SneakingState;
import com.asangarin.breaker.states.TimeState;
import com.asangarin.breaker.states.ToolState;
import com.asangarin.breaker.states.WaterState;
import com.asangarin.breaker.states.WeatherState;
import com.asangarin.breaker.states.WorldState;
import com.asangarin.breaker.utility.BreakState;
import com.asangarin.breaker.utility.Manager;

public class StatesManager extends Manager<BreakState> {
	@Override
	public void load() {
		register(EnchantmentState.class);
		register(ExperienceState.class);
		register(FlyingState.class);
		register(PotionState.class);
		register(SneakingState.class);
		register(TimeState.class);
		register(ToolState.class);
		register(WaterState.class);
		register(WeatherState.class);
		register(WorldState.class);
		if(Breaker.plugin.isLoaded("MMOItems")) register(MMOItemState.class);
		if(Breaker.plugin.isLoaded("MMOCore")) {
			register(ClassState.class);
			register(ProfessionState.class);
			register(ResourceState.class);
		}
		if(Breaker.plugin.isLoaded("WorldGuard")) register(RegionState.class);
	}
}
