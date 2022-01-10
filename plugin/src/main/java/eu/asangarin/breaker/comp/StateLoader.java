package eu.asangarin.breaker.comp;

import eu.asangarin.breaker.api.BreakerAPI;
import eu.asangarin.breaker.comp.mmocore.AttributeState;
import eu.asangarin.breaker.comp.mmocore.ClassState;
import eu.asangarin.breaker.comp.mmocore.MMOExperienceState;
import eu.asangarin.breaker.comp.mmocore.PointState;
import eu.asangarin.breaker.comp.mmocore.ProfessionState;
import eu.asangarin.breaker.comp.mmocore.ResourceState;
import eu.asangarin.breaker.comp.mythicmobs.MythicCanCastState;
import eu.asangarin.breaker.comp.mythicmobs.MythicVariableState;
import eu.asangarin.breaker.comp.techtree.TechEntryState;
import eu.asangarin.breaker.comp.worldguard.RegionState;

public class StateLoader {
	public static void loadMMOCore() {
		BreakerAPI.registerState("mmoexp", MMOExperienceState.class);
		BreakerAPI.registerState("mmoattribute", AttributeState.class);
		BreakerAPI.registerState("mmoclass", ClassState.class);
		BreakerAPI.registerState("mmopoint", PointState.class);
		BreakerAPI.registerState("mmoprof", ProfessionState.class);
		BreakerAPI.registerState("mmoresource", ResourceState.class);
	}

	public static void loadMythicMobs() {
		BreakerAPI.registerState("mmvar", MythicVariableState.class);
		BreakerAPI.registerState("mmcast", MythicCanCastState.class);
	}

	public static void loadTechTree() {
		BreakerAPI.registerState("techentry", TechEntryState.class);
	}

	public static void loadWorldGuard() {
		BreakerAPI.registerState("region", RegionState.class);
	}
}
