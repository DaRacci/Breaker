package eu.asangarin.breaker.system;

import eu.asangarin.breaker.Breaker;
import eu.asangarin.breaker.api.BreakerState;
import eu.asangarin.breaker.network.BlockDigPacketInfo;
import eu.asangarin.breaker.util.BlockFile;
import eu.asangarin.breaker.util.TriggerType;
import io.lumine.mythic.utils.config.LineConfig;
import io.lumine.mythic.utils.config.properties.Property;
import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
public class DatabaseBlock {
	private final int min, max, base;
	private final List<BreakerState> states = new ArrayList<>();
	private final Map<TriggerType, List<BreakerTrigger.TriggerTrigger>> triggers = BreakerTrigger.newTriggerMap();

	public DatabaseBlock(BlockFile file) {
		this.max = Property.Int(file, "breaking-time.max", 40).get();
		this.min = Property.Int(file, "breaking-time.min", 20).get();
		this.base = Property.Int(file, "breaking-time.base", max).get();
		for (String conf : Property.StringList(file, "states").get())
			Breaker.get().getBreakerStates().fromConfig(file.get().substring(7), conf).ifPresent(states::add);
		for (String conf : Property.StringList(file, "triggers").get())
			fromConfig(file.get().substring(7), conf).ifPresent(trigger -> triggers.get(trigger.getType()).add(trigger.prepare()));
	}

	private Optional<BreakerTrigger> fromConfig(String name, String input) {
		BreakerTrigger trigger = new BreakerTrigger();
		if (trigger.validate(name, new LineConfig(input))) return Optional.of(trigger);
		return Optional.empty();
	}

	public int calculateBreakTime(BlockDigPacketInfo info) {
		Player player = info.getPlayer().get();
		Block block = info.getBlock();
		if (player == null || block == null) return -1;

		int breakTime = base;
		for (BreakerState state : states)
			if (state.isConditionMet(player, block)) breakTime -= state.getDeduction();

		return Math.min(max, Math.max(min, breakTime));
	}

	public boolean canMine(BlockDigPacketInfo info) {
		Player player = info.getPlayer().get();
		Block block = info.getBlock();

		for (BreakerState state : states)
			if (state.isRequired() && !state.isConditionMet(player, block)) return false;

		return true;
	}

	@Override
	public String toString() {
		return String.format("[%d, %d, %d]", max, min, base);
	}

	public void trigger(TriggerType start, BlockDigPacketInfo info) {
		Player player = info.getPlayer().get();
		if (player == null) return;

		triggers.get(start).forEach(t -> t.trigger(player, info.getBlock()));
	}
}
