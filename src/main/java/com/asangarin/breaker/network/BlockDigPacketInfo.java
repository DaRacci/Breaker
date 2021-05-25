package com.asangarin.breaker.network;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.lang.ref.WeakReference;

@Getter
public class BlockDigPacketInfo {
	private final WeakReference<Player> player;
	private final int x, y, z;
	private final DigType digType;

	public BlockDigPacketInfo(Player player, int x, int y, int z, String name) {
		this.player = new WeakReference<>(player);
		this.x = x;
		this.y = y;
		this.z = z;
		this.digType = fromName(name);
	}

	/*
	 * Creates a new Bukkit Location from this objects coordinates
	 * @return ^
	public World getWorld() {
		Player p = player.get();
		if(p == null) return null;
		return new Location(p.getWorld(), x, y, z);
	}*/

	/**
	 * Check if the coordinates of a location match the coordinates of this object
	 * @param loc The location to check against
	 * @return ^
	 */
	public boolean compareLocation(Location loc) {
		return x == loc.getBlockX() && y == loc.getBlockY() && z == loc.getBlockZ();
	}

	/**
	 * Constructs a unique entity id from this objects coordinates
	 * @return ^
	 */
	public int getEntityId() {
		return ((x & 0xFFF) << 20) | ((z & 0xFFF) << 8) | (y & 0xFF);
	}

	public boolean hasReference() {
		return player.get() != null;
	}

	public Block getBlock() {
		Player p = player.get();
		if(p == null) return null;
		return p.getWorld().getBlockAt(x, y, z);
	}

	// A wrapper to make things more simple
	public enum DigType {
		START,
		STOP,
		ABORT,
		INVALID;
	}

	/**
	 * Returns true if this object has digType value of DigType.ABORT or DigType.STOP
	 * @return ^
	 */
	public boolean didFinish() {
		return digType == DigType.ABORT || digType == DigType.STOP;
	}

	/* A little convenience method that gets a DigType object from the names
	   of the NMS PlayerDigType enum */
	private DigType fromName(String name) {
		switch(name.toUpperCase()) {
			case "START_DESTROY_BLOCK":
				return DigType.START;
			case "STOP_DESTROY_BLOCK":
				return DigType.STOP;
			case "ABORT_DESTROY_BLOCK":
				return DigType.ABORT;
			default:
				return DigType.INVALID;
		}
	}

	@Override
	public String toString() {
		return String.format("[%s] - %d, %d, %d", digType.toString(), x, y, z);
	}
}
