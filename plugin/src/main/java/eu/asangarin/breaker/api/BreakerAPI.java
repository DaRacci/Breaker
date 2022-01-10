package eu.asangarin.breaker.api;

import eu.asangarin.breaker.Breaker;

public class BreakerAPI {
	public static void registerState(String key, Class<? extends BreakerState> clazz) {
		Breaker.get().getBreakerStates().register(key, clazz);
	}

	public static void registerBlockProvider(IBlockProvider provider) {
		Breaker.get().getBlockProviders().register(provider);
	}
}
