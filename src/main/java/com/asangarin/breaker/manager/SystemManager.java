package com.asangarin.breaker.manager;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.api.BreakerSystem;
import com.asangarin.breaker.api.Manager;
import com.asangarin.breaker.system.MMOItemsSystem;
import com.asangarin.breaker.system.MaterialSystem;
import com.asangarin.breaker.system.SkullSystem;

public class SystemManager extends Manager<BreakerSystem> {
	@Override
	public void load() {
		register(MaterialSystem.class);
		register(SkullSystem.class);
		if(Breaker.plugin.isLoaded("MMOItems") && !Breaker.plugin.nms.getVersion().equals("1.12_R1")) register(MMOItemsSystem.class);
	}
}
