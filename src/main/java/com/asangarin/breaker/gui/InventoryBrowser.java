package com.asangarin.breaker.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class InventoryBrowser extends BaseInventory {
	public InventoryBrowser(Player player) {
		super(player, 54, "Configuration Browser");
	}

	@Override
	public Inventory getInventory(Inventory inv) {
		return inv;
	}

	@Override
	public void whenClicked(InventoryClickEvent event) {
	}
}
