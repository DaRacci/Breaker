package eu.asangarin.breaker.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum TriggerType {
	START, ABORT, BREAK, STOP;

	public static final Set<String> TYPES = new HashSet<>(Arrays.asList("start", "abort", "break", "stop"));
	public static final TriggerType[] ALL = {START, ABORT, BREAK, STOP};
}
