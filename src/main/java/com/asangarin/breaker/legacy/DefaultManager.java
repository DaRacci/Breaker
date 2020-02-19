package com.asangarin.breaker.legacy;

import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DefaultManager extends VersionWrapper {
	@Override
	public PotionEffect getPotionEffect(int amount) {
		return new PotionEffect(PotionEffectType.SLOW_DIGGING, amount, -1, false, false, false);
	}

	@Override
	public void spawnBlockParticle(Block block) {
		block.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation().add(0.5, 0.5, 0.5), 100, 0.1, 0.1, 0.1, 4, block.getBlockData());
	}
}
