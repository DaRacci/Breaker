package eu.asangarin.breaker.states;

import eu.asangarin.breaker.Breaker;
import eu.asangarin.breaker.api.BreakerState;
import io.lumine.mythic.bukkit.utils.config.LineConfig;
import io.lumine.mythic.core.config.MythicLineConfigImpl;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NestedStates extends BreakerState {
	private final List<BreakerState> states = new ArrayList<>();

	@Override
	public boolean isConditionMet(Player breaker, Block block) {
		for (BreakerState state : states) {
			System.out.println(state.getClass().getName());
			System.out.println("Is Met: " + state.isConditionMet(breaker, block));

			if (!state.isConditionMet(breaker, block)) return false;
		}
		return true;
	}

	@Override
	protected boolean setup(LineConfig config) {
		String nestedStates = config.getString("states");

		if (nestedStates == null) return error("'states' is missing the states arg!");
		nestedStates = nestedStates.trim();
		System.out.println(nestedStates);

		if (nestedStates.startsWith("[") && nestedStates.endsWith("]")) {
			nestedStates = nestedStates.substring(1, nestedStates.length() - 1);

			nestedStates = MythicLineConfigImpl.unparseBlock(nestedStates);

			System.out.println(nestedStates);
			final String[] split = nestedStates.split("-");

			List<String> elements = Arrays.stream(split).map(String::trim).filter(trim -> trim.length() != 0)
					.filter(trim -> !trim.startsWith("<#>")).collect(Collectors.toList());

			for (String line : elements) {
				System.out.println(line);
				Breaker.get().getBreakerStates().fromConfig("Nested State", line).ifPresent(states::add);
			}
		} else {
			return error("'states' arg is not a valid array!");
		}

		return true;
	}
}
