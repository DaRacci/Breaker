package eu.asangarin.breaker.comp;

import eu.asangarin.breaker.Breaker;
import eu.asangarin.breaker.api.BreakerAPI;
import eu.asangarin.breaker.comp.vault.MoneyState;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultCompat {
	@Getter
	private static Economy econ = null;

	public static void setup() {
		if (!setupEconomy()) {
			Breaker.error("Vault support disabled due to no economy plugin found!");
			return;
		}

		BreakerAPI.registerState("money", MoneyState.class);
	}

	private static boolean setupEconomy() {
		RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
		if (rsp == null) return false;
		econ = rsp.getProvider();
		return econ != null;
	}
}
