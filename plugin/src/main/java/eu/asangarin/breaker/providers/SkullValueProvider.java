package eu.asangarin.breaker.providers;

import eu.asangarin.breaker.Breaker;
import eu.asangarin.breaker.api.IBlockProvider;
import io.lumine.mythic.bukkit.utils.Schedulers;
import io.lumine.mythic.bukkit.utils.promise.Promise;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class SkullValueProvider implements IBlockProvider {
	@Override
	public Collection<String> getKeys(Block block) {
		Promise<Collection<String>> promise = Schedulers.sync().supply(() -> {
			if (block.getState() instanceof Skull)
				return Collections.singletonList("SKULL_" + Breaker.get().getPackkit().getNMS().getSkullValue(block));
			return Collections.emptyList();
		});

		try {
			return promise.get();
		} catch (InterruptedException | ExecutionException e) {
			return Collections.emptyList();
		}
	}
}
