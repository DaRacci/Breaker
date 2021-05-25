package com.asangarin.breaker.system;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.network.BlockDigPacketInfo;
import com.asangarin.packkit.Packkit;
import io.lumine.mythic.utils.Schedulers;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class BreakingSystem implements Listener {
	private final Map<UUID, BreakerBlock> activeBlocks = new HashMap<>();
	// This list contains materials that will never be handled by Breaker.
	private final List<Material> excludedMaterials = new ArrayList<>();

	public void load() {
		// Add all potted plants*
		for (Material mat : Material.values())
			if (mat.name().contains("POTTED")) excludedMaterials.add(mat);
		// Basic materials that shouldn't change in future versions
		excludedMaterials.addAll(Arrays.asList(Material.AIR, Material.GRASS, Material.TALL_GRASS, Material.END_ROD, Material.BARRIER, Material.BRAIN_CORAL, Material.BRAIN_CORAL_FAN, Material.BUBBLE_CORAL, Material.BUBBLE_CORAL_FAN, Material.FIRE_CORAL, Material.FIRE_CORAL_FAN, Material.HORN_CORAL, Material.HORN_CORAL_FAN, Material.TUBE_CORAL, Material.TUBE_CORAL_FAN, Material.DEAD_BRAIN_CORAL, Material.DEAD_BRAIN_CORAL_FAN, Material.DEAD_BUBBLE_CORAL, Material.DEAD_BUBBLE_CORAL_FAN, Material.DEAD_FIRE_CORAL, Material.DEAD_FIRE_CORAL_FAN, Material.DEAD_HORN_CORAL, Material.DEAD_HORN_CORAL_FAN, Material.DEAD_TUBE_CORAL, Material.DEAD_TUBE_CORAL_FAN, Material.TORCH, Material.REDSTONE_TORCH, Material.WALL_TORCH, Material.REDSTONE_WALL_TORCH, Material.FERN, Material.LARGE_FERN, Material.BEETROOTS, Material.WHEAT, Material.POTATOES, Material.CARROTS, Material.OAK_SAPLING, Material.DARK_OAK_SAPLING, Material.SPRUCE_SAPLING, Material.ACACIA_SAPLING, Material.BIRCH_SAPLING, Material.JUNGLE_SAPLING, Material.FLOWER_POT, Material.POPPY, Material.DANDELION, Material.ALLIUM, Material.BLUE_ORCHID, Material.AZURE_BLUET, Material.RED_TULIP, Material.ORANGE_TULIP, Material.WHITE_TULIP, Material.PINK_TULIP, Material.OXEYE_DAISY, Material.CORNFLOWER, Material.LILY_OF_THE_VALLEY, Material.WITHER_ROSE, Material.SUNFLOWER, Material.LILAC, Material.ROSE_BUSH, Material.PEONY, Material.LILY_PAD, Material.FIRE, Material.DEAD_BUSH, Material.MELON_STEM, Material.PUMPKIN_STEM, Material.BROWN_MUSHROOM, Material.RED_MUSHROOM, Material.NETHER_WART, Material.REDSTONE_WIRE, Material.COMPARATOR, Material.REPEATER, Material.SLIME_BLOCK, Material.STRUCTURE_VOID, Material.SUGAR_CANE, Material.TNT, Material.TRIPWIRE, Material.TRIPWIRE_HOOK, Material.WARPED_FUNGUS, Material.CRIMSON_FUNGUS, Material.HONEY_BLOCK, Material.NETHER_SPROUTS, Material.CRIMSON_ROOTS, Material.WARPED_ROOTS, Material.TWISTING_VINES_PLANT, Material.WEEPING_VINES_PLANT));
		// Minecraft Version exclusive materials for the current version
		excludedMaterials.addAll(Breaker.get().getPackkit().getNMS().getExcludedBlocks());
	}

	/*
	 * Make all the calculations that are needed once a break packet has been received.
	 * In our case, we don't need the packet to be cancelled so we just return true.
	 */
	public boolean compute(BlockDigPacketInfo info) {
		Player player = info.getPlayer().get();
		if(player == null || isExcluded(info)) return true;
		/* If the block breaking was just initialized we want to add the potion effect and
		   register the player and block to the activeBlocks map. */
		if (info.getDigType() == BlockDigPacketInfo.DigType.START) {
			Optional<DatabaseBlock> dbBlock = Breaker.get().getDatabase().fromBlock(info.getBlock());

			if(!dbBlock.isPresent()) return true;
			activeBlocks.put(player.getUniqueId(), new BreakerBlock(info, dbBlock.get()));
			Schedulers.sync().run(() -> player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, -1, false, false, false)));
		}

		return true;
	}

	/*
	 * Runs after the packet has been properly handled by the server.
	 * This code is ran at this stage to prevent unexpected bugs.
	 */
	public void conclude(BlockDigPacketInfo info) {
		Player player = info.getPlayer().get();
		/* No conclusion is needed if the block isn't handled by Breaker
		   or wasn't broken or the action wasn't aborted. */
		if (!info.didFinish() || player == null || isExcluded(info)) return;
		/* Only remove the player if the action was aborted. That way, we can check if the block was
		   handled properly by Breaker or if any exploits had been attempted. */
		if (info.getDigType() == BlockDigPacketInfo.DigType.ABORT) {
			// If there's a currently active block, simply stop the process and remove the active block.
			if(activeBlocks.containsKey(player.getUniqueId())) {
				activeBlocks.get(player.getUniqueId()).stop();
				activeBlocks.remove(player.getUniqueId());
			}
			// Remove the potion effect
			Schedulers.sync().run(() -> player.removePotionEffect(PotionEffectType.SLOW_DIGGING));
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	private void blockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		// If the player is creative or block broken isn't supposed to be handled by breaker then just return.
		if (player.getGameMode() == GameMode.CREATIVE || !isBreakerBlock(event.getBlock())) return;
		/* Check if the block was handled properly then just return
		   and remove the player from activeBlocks if that's the case. */
		if (activeBlocks.containsKey(player.getUniqueId())) {
			BreakerBlock breakerBlock = activeBlocks.get(player.getUniqueId());
			if (breakerBlock.getInfo().compareLocation(event.getBlock().getLocation())) {
				activeBlocks.remove(player.getUniqueId());
				return;
			}
		}

		/* If we reach this part, that means the block wasn't broken normally, so we cancel the event */
		event.setCancelled(true);
	}

	// Checks if the block is supposed to be handled by Breaker
	private boolean isBreakerBlock(Block block) {
		return !isExcluded(block) && Breaker.get().getDatabase().shouldBlockBeHandled(block);
	}

	private boolean isExcluded(BlockDigPacketInfo info) {
		return isExcluded(info.getBlock());
	}

	// Checks if the block is of a special excluded block type
	private boolean isExcluded(Block block) {
		return block == null || excludedMaterials.contains(block.getType());
	}

	public void sendBreakAnimationPacket(BlockDigPacketInfo info, int stage) {
		Player player = info.getPlayer().get();
		if(player == null) return;
		Packkit packkit = Breaker.get().getPackkit();
		packkit.sendPacket(player, packkit.getNMS().createDigAnimationPacket(info, stage));
	}
}
