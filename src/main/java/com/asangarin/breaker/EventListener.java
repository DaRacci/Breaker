package com.asangarin.breaker;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffectType;

import com.asangarin.breaker.api.BreakerSystem;
import com.asangarin.breaker.gui.BaseInventory;

public class EventListener implements Listener {
	@EventHandler
	public void playerJoin(PlayerJoinEvent e) {
		e.getPlayer().addPotionEffect(Breaker.plugin.legacy
				.getPotionEffect(Settings.instance().permanentMiningFatigue() ? Integer.MAX_VALUE : 120));
	}

	@EventHandler
	public void playerRespawn(PlayerRespawnEvent e) {
		e.getPlayer().addPotionEffect(Breaker.plugin.legacy
				.getPotionEffect(Settings.instance().permanentMiningFatigue() ? Integer.MAX_VALUE : 120));
	}

	@EventHandler
	public void inventoryClick(InventoryClickEvent e) {
		if (e.getInventory().getHolder() instanceof BaseInventory)
			((BaseInventory) e.getInventory().getHolder()).whenClicked(e);
	}

	@EventHandler
	public void blockDamage(BlockDamageEvent e) {
		// Testing how different events treat block breaking.
		Breaker.debug("BlockDamageEvent: " + Breaker.plugin.core.contains(e.getBlock()), 5);

		for (BreakerSystem s : Breaker.plugin.core.getActiveSystems())
			if (Breaker.plugin.database.has(s.getId(e.getBlock()))) {
				e.getPlayer().addPotionEffect(Breaker.plugin.legacy.getPotionEffect(Integer.MAX_VALUE));
				if (e.getInstaBreak()) {
					Breaker.plugin.core.caught(e.getBlock());
					e.setCancelled(true);
					if (e.getPlayer().hasPotionEffect(PotionEffectType.SLOW_DIGGING))
						e.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
				}
			}
	}

	@EventHandler
	public void playerConsumeItem(PlayerItemConsumeEvent e) {
		if (!(e.getItem().getType() == Material.MILK_BUCKET))
			return;
		if (!Settings.instance().permanentMiningFatigue())
			return;
		Bukkit.getScheduler().runTaskLater(Breaker.plugin, new Runnable() {
			@Override
			public void run() {
				e.getPlayer().addPotionEffect(Breaker.plugin.legacy.getPotionEffect(Integer.MAX_VALUE));
			}
		}, 2);
	}

	@EventHandler
	public void c(BlockBreakEvent e) {
		// more testing
		Breaker.debug("BlockBreakEvent: " + Breaker.plugin.core.contains(e.getBlock()), 5);
	}
}
