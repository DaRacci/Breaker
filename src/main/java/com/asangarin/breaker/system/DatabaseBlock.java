package com.asangarin.breaker.system;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.api.BreakerState;
import com.asangarin.breaker.network.BlockDigPacketInfo;
import io.lumine.mythic.utils.config.properties.Property;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DatabaseBlock {
	private final BlockHardness hardness;
	private final List<BreakerState> states = new ArrayList<>();

	public DatabaseBlock(Object file) {
		int max = Property.Int(Breaker.get(), file, "breaking-time.max", 40).get();
		int min = Property.Int(Breaker.get(), file, "breaking-time.min", 20).get();
		int base = Property.Int(Breaker.get(), file, "breaking-time.base", max).get();
		this.hardness = new BlockHardness(min, max, base);
		for (String conf : Property.StringList(Breaker.get(), file, "states").get())
			Breaker.get().getBreakerStates().fromConfig(conf).ifPresent(states::add);
	}

	public int calculateBreakTime(BlockDigPacketInfo info) {
		Player player = info.getPlayer().get();
		Block block = info.getBlock();
		if (player == null || block == null) return -1;

		int breakTime = hardness.getBase();
		for (BreakerState state : states)
			if (state.isConditionMet(player, block)) breakTime -= state.getDeduction();

		return Math.min(hardness.getMax(), Math.max(hardness.getMin(), breakTime));
	}
}
