package com.asangarin.breaker.manager;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.api.BreakTrigger;
import com.asangarin.breaker.api.Manager;
import com.asangarin.breaker.comp.mmocore.EXPTrigger;
import com.asangarin.breaker.comp.mmocore.RestoreTrigger;
import com.asangarin.breaker.triggers.CommandTrigger;
import com.asangarin.breaker.triggers.FireTrigger;
import com.asangarin.breaker.triggers.PotionTrigger;

public class TriggerManager extends Manager<BreakTrigger> {
	@Override
	public void load() {
		register(FireTrigger.class);
		register(PotionTrigger.class);
		register(CommandTrigger.class);
		if(Breaker.plugin.isLoaded("MMOCore")) {
			register(EXPTrigger.class);
			register(RestoreTrigger.class);
		}
	}
}
