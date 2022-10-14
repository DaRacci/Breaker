package eu.asangarin.breaker.comp.crucible;

import eu.asangarin.breaker.api.IBlockProvider;
import io.lumine.mythiccrucible.MythicCrucible;
import io.lumine.mythiccrucible.items.blocks.CustomBlockItemContext;
import org.bukkit.block.Block;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class CrucibleBlockProvider implements IBlockProvider {
	@Override
	public Collection<String> getKeys(Block block) {
		Optional<CustomBlockItemContext> cBlock = MythicCrucible.inst().getItemManager().getCustomBlockManager().getBlockFromBlock(block);
		return cBlock.map(customBlock -> Collections.singletonList("CRUCIBLE_" + customBlock.getBlockId())).orElse(Collections.emptyList());
	}
}
