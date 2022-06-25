package eu.asangarin.breaker.util;

import io.lumine.mythic.bukkit.utils.config.properties.PropertyScope;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BlockFile implements PropertyScope {
	private final String file;

	@Override
	public String get() {
		return file;
	}
}
