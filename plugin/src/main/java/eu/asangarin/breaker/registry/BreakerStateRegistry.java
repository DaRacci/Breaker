package eu.asangarin.breaker.registry;

import eu.asangarin.breaker.Breaker;
import eu.asangarin.breaker.api.BreakerState;
import eu.asangarin.breaker.states.HeldItemState;
import eu.asangarin.breaker.states.SneakingState;
import io.lumine.mythic.utils.config.LineConfig;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BreakerStateRegistry {
	private final Map<String, Class<? extends BreakerState>> states = new HashMap<>();

	public BreakerStateRegistry() {
		register("helditem", HeldItemState.class);
		register("sneaking", SneakingState.class);
	}

	public void register(String key, Class<? extends BreakerState> state) {
		states.put(key, state);
	}

	public Optional<BreakerState> fromConfig(String filename, String input) {
		LineConfig config = new LineConfig(input);
		if (!states.containsKey(config.getKey())) {
			Breaker.error("[" + filename + "] Couldn't add state: '" + config.getKey() + "' is not a valid breakstate");
			return Optional.empty();
		}
		try {
			BreakerState state = states.get(config.getKey()).getDeclaredConstructor().newInstance();
			state.setName(filename);
			if (state.create(config)) return Optional.of(state);
		} catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
			Breaker.error("Something went wrong adding a breakstate! Please report this to the plugin author.");
			e.printStackTrace();
		}

		return Optional.empty();
	}
}
