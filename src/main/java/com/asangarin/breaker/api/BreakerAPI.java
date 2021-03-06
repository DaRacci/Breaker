package com.asangarin.breaker.api;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.SystemManager;

public class BreakerAPI {
	public static SystemManager getSystemManager() {
		return Breaker.get().getSystemManager();
	}
}
