package eu.asangarin.breaker.states;

import eu.asangarin.breaker.api.BreakerState;
import io.lumine.mythic.bukkit.utils.config.LineConfig;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class TimeState extends BreakerState {
	private long min, max;

	@Override
	public boolean isConditionMet(Player breaker, Block block) {
		World world = block.getWorld();
		return world.getTime() >= min && world.getTime() < max;
	}

	@Override
	protected boolean setup(LineConfig config) {
		String minS = config.getString("min");
		if (minS == null) return error("'time' is missing the min arg!");
		String maxS = config.getString("max");
		if (maxS == null) return error("'time' is missing the max arg!");
		this.min = convertTime(minS);
		this.max = convertTime(maxS);

		if ((min >= 0 && min < 24000) || (max > 0 && max < 24001)) return true;

		return error("'time' doesn't have a correct time value!");
	}

	private long convertTime(String time) {
		if (time.matches("\\d+")) return Math.min(24000, Math.max(0, Long.parseLong(time)));

		if (time.equalsIgnoreCase("day")) return 1000;
		if (time.equalsIgnoreCase("noon")) return 6000;
		if (time.equalsIgnoreCase("sunset")) return 12000;
		if (time.equalsIgnoreCase("night")) return 13000;
		if (time.equalsIgnoreCase("midnight")) return 18000;
		if (time.equalsIgnoreCase("sunrise")) return 23000;
		return -1;
	}

}
