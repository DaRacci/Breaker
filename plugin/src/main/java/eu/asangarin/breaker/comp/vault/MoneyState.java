package eu.asangarin.breaker.comp.vault;

import eu.asangarin.breaker.api.BreakerState;
import io.lumine.mythic.utils.config.LineConfig;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class MoneyState extends BreakerState {
	private double money;

	@Override
	public boolean isConditionMet(Player breaker, Block block) {
		return VaultCompat.getEcon().hasAccount(breaker) && VaultCompat.getEcon().getBalance(breaker) >= money;
	}

	@Override
	protected boolean setup(LineConfig config) {
		money = config.getDouble("amount");
		if (money == 0) return error("'money' is missing the amount arg (or it is 0)!");
		return true;
	}
}
