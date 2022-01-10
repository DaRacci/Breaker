package eu.asangarin.breaker.providers;

import eu.asangarin.breaker.api.IBlockProvider;
import io.lumine.mythic.lib.MythicLib;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;

import java.util.Collection;
import java.util.Collections;

public class SkullValueProvider implements IBlockProvider {
	@Override
	public Collection<String> getKeys(Block block) {
		if (block.getState() instanceof Skull)
			return Collections.singletonList(MythicLib.plugin.getVersion().getWrapper().getSkullValue(block));
		return Collections.emptyList();
	}
}
