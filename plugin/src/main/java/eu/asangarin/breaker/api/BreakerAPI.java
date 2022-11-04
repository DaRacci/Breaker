package eu.asangarin.breaker.api;

import eu.asangarin.breaker.Breaker;
import eu.asangarin.breaker.system.MutableDatabaseBlock;
import java.util.function.Consumer;
import org.bukkit.Material;

public class BreakerAPI {
	public static void registerState(String key, Class<? extends BreakerState> clazz) {
		Breaker.get().getBreakerStates().register(key, clazz);
	}

	public static void registerBlock(Material material, Consumer<MutableDatabaseBlock> mutator) {
		final var block = Breaker.get().getDatabase().fromKey(material.name())
			.map(MutableDatabaseBlock::from)
			.orElseGet(MutableDatabaseBlock::new);

		mutator.accept(block);

		Breaker.get().getDatabase().put(material.name(), block.toImmutable());
	}

	public static void registerBlockProvider(IBlockProvider provider) {
		Breaker.get().getBlockProviders().register(provider);
	}
}
