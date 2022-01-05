package eu.asangarin.breaker.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum TriggerFunction {
	COMMAND, SKILL, EVENT;

	public static final Set<String> FUNCTIONS = new HashSet<>(Arrays.asList("command", "skill", "event"));
}
