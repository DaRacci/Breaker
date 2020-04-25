package com.asangarin.breaker.manager;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.api.BreakerSystem;
import com.asangarin.breaker.api.Manager;
import com.asangarin.breaker.system.MMOItemsSystem;
import com.asangarin.breaker.system.MaterialSystem;

public class SystemManager extends Manager<BreakerSystem> {
	@Override
	public void load() {
		register(MaterialSystem.class);
		if(Breaker.plugin.isLoaded("MMOItems") && !Breaker.plugin.nms.getVersion().equals("1.12_R1")) register(MMOItemsSystem.class);
	}
}
