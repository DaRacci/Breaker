package eu.asangarin.breaker.api;

import eu.asangarin.breaker.Breaker;
import io.lumine.mythic.bukkit.utils.config.LineConfig;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public abstract class BreakerState {
	@Setter
	private String name = "UNKNOWN";
	protected int value = 1;
	@Getter
	protected boolean required = false;

	public int getDeduction() {
		return value;
	}

	public abstract boolean isConditionMet(Player breaker, Block block);

	protected boolean setup(LineConfig config) {
		return true;
	}

	public boolean create(LineConfig config) {
		value = config.getInteger("value", 1);
		required = config.getBoolean("required", false);
		return setup(config);
	}

	protected boolean error(String arg) {
		Breaker.error("[" + name + "] Couldn't add state: " + arg);
		return false;
	}
}
