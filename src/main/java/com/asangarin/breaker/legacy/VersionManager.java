package com.asangarin.breaker.legacy;

import org.bukkit.block.Block;
import org.bukkit.potion.PotionEffect;

public abstract class VersionManager {
	public abstract PotionEffect getPotionEffect(int amount);
	public abstract void spawnBlockParticle(Block block);
}
