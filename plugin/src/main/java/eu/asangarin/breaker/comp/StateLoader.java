package eu.asangarin.breaker.comp;

import eu.asangarin.breaker.Breaker;
import eu.asangarin.breaker.comp.mmocore.MMOExperienceState;
import eu.asangarin.breaker.comp.mythicmobs.MythicVariableState;
import eu.asangarin.breaker.comp.techtree.TechEntryState;

public class StateLoader {
	public static void loadMMOCore() {
		Breaker.get().getBreakerStates().register("mmoexp", MMOExperienceState.class);
	}

	public static void loadMythicMobs() {
		Breaker.get().getBreakerStates().register("mmvar", MythicVariableState.class);
	}

	public static void loadTechTree() {
		Breaker.get().getBreakerStates().register("techentry", TechEntryState.class);
	}

	public static void loadWorldGuard() {

	}
}
