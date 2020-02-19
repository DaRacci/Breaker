package com.asangarin.breaker.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import net.Indyuce.mmoitems.MMOItems;

//Credits to Indy for this class
public abstract class BaseInventory implements InventoryHolder {
	protected Player player;
	protected int size;
	protected String title;

	public BaseInventory(Player player, int size, String title)
	{ this.player = player; this.size = size; this.title = title; }
	public Player getPlayer()
	{ return player; }

	@Override
	public Inventory getInventory() {
		Inventory inv = Bukkit.createInventory(this, size, ChatColor.UNDERLINE + title);
		return getInventory(inv);
	}

	public abstract void whenClicked(InventoryClickEvent event);

	public void open() {
		if (Bukkit.isPrimaryThread()) getPlayer().openInventory(getInventory());
		else Bukkit.getScheduler().runTask(MMOItems.plugin, () -> getPlayer().openInventory(getInventory()));
	}
	
	public abstract Inventory getInventory(Inventory inv);
}
