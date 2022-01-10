package eu.asangarin.breaker.comp.mmoitems;

import eu.asangarin.breaker.api.IBlockProvider;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.block.CustomBlock;
import org.bukkit.block.Block;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class MIBlockProvider implements IBlockProvider {
	@Override
	public Collection<String> getKeys(Block block) {
		Optional<CustomBlock> cBlock = MMOItems.plugin.getCustomBlocks().getFromBlock(block.getBlockData());
		return cBlock.map(customBlock -> Collections.singletonList("MMOITEMS_" + customBlock.getId())).orElse(Collections.emptyList());
	}
}
