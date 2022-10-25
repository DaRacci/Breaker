package eu.asangarin.breaker.registry;

import eu.asangarin.breaker.Breaker;
import eu.asangarin.breaker.api.BreakerState;
import eu.asangarin.breaker.comp.mythicmobs.MythicCanCastState;
import eu.asangarin.breaker.comp.mythicmobs.MythicVariableState;
import eu.asangarin.breaker.states.EffectState;
import eu.asangarin.breaker.states.EnchantState;
import eu.asangarin.breaker.states.ExperienceState;
import eu.asangarin.breaker.states.HeldItemState;
import eu.asangarin.breaker.states.InAirState;
import eu.asangarin.breaker.states.InWaterState;
import eu.asangarin.breaker.states.NestedStates;
import eu.asangarin.breaker.states.PermissionState;
import eu.asangarin.breaker.states.SneakingState;
import eu.asangarin.breaker.states.TimeState;
import eu.asangarin.breaker.states.UnbreakableState;
import eu.asangarin.breaker.states.WeatherState;
import eu.asangarin.breaker.states.WorldState;
import eu.asangarin.breaker.states.nbt.NBTBooleanState;
import eu.asangarin.breaker.states.nbt.NBTStringState;
import eu.asangarin.breaker.states.nbt.NBTValueState;
import eu.asangarin.breaker.util.BreakerSettings;
import io.lumine.mythic.bukkit.utils.config.LineConfig;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BreakerStateRegistry {
	private final Map<String, Class<? extends BreakerState>> states = new HashMap<>();

	public BreakerStateRegistry() {
		register("states", NestedStates.class);

		register("effect", EffectState.class);
		register("enchant", EnchantState.class);
		register("exp", ExperienceState.class);
		register("helditem", HeldItemState.class);
		register("air", InAirState.class);
		register("water", InWaterState.class);
		register("sneaking", SneakingState.class);
		register("time", TimeState.class);
		register("unbreakable", UnbreakableState.class);
		register("rain", WeatherState.class);
		register("world", WorldState.class);
		register("permission", PermissionState.class);

		// MythicMobs
		register("mmvar", MythicVariableState.class);
		register("mmcast", MythicCanCastState.class);

		// NBT
		register("nbtstr", NBTStringState.class);
		register("nbtbool", NBTBooleanState.class);
		register("nbtval", NBTValueState.class);
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

			//TODO Implement
			if(!BreakerSettings.get().isUnstable() && state instanceof NestedStates) {
				Breaker.error("[" + filename + "] Couldn't add state: '" + config.getKey() + "' - Unstable Mode is not enabled!");
				return Optional.empty();
			}

			state.setName(filename);
			if (state.create(config)) return Optional.of(state);
		} catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
			Breaker.error("Something went wrong adding a breakstate! Please report this to the plugin author.");
			e.printStackTrace();
		}

		return Optional.empty();
	}
}
