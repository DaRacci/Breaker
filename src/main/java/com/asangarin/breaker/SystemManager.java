package com.asangarin.breaker;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public class SystemManager {
	public List<Material> excludedMaterials = Arrays.asList(Material.AIR, Material.GRASS, Material.TALL_GRASS,
			Material.END_ROD, Material.BARRIER, Material.BRAIN_CORAL, Material.BRAIN_CORAL_FAN, Material.BUBBLE_CORAL,
			Material.BUBBLE_CORAL_FAN, Material.FIRE_CORAL, Material.FIRE_CORAL_FAN, Material.HORN_CORAL,
			Material.HORN_CORAL_FAN, Material.TUBE_CORAL, Material.TUBE_CORAL_FAN, Material.DEAD_BRAIN_CORAL,
			Material.DEAD_BRAIN_CORAL_FAN, Material.DEAD_BUBBLE_CORAL, Material.DEAD_BUBBLE_CORAL_FAN,
			Material.DEAD_FIRE_CORAL, Material.DEAD_FIRE_CORAL_FAN, Material.DEAD_HORN_CORAL,
			Material.DEAD_HORN_CORAL_FAN, Material.DEAD_TUBE_CORAL, Material.DEAD_TUBE_CORAL_FAN,
			Material.TORCH, Material.REDSTONE_TORCH, Material.WALL_TORCH, Material.REDSTONE_WALL_TORCH,
			Material.FERN, Material.LARGE_FERN, Material.BEETROOTS, Material.WHEAT, Material.POTATOES,
			Material.CARROTS, Material.OAK_SAPLING, Material.DARK_OAK_SAPLING, Material.SPRUCE_SAPLING,
			Material.ACACIA_SAPLING, Material.BIRCH_SAPLING, Material.JUNGLE_SAPLING, Material.FLOWER_POT,
			Material.POPPY, Material.DANDELION, Material.ALLIUM, Material.BLUE_ORCHID, Material.AZURE_BLUET,
			Material.RED_TULIP, Material.ORANGE_TULIP, Material.WHITE_TULIP, Material.PINK_TULIP, Material.OXEYE_DAISY,
			Material.CORNFLOWER, Material.LILY_OF_THE_VALLEY, Material.WITHER_ROSE, Material.SUNFLOWER, Material.LILAC,
			Material.ROSE_BUSH, Material.PEONY, Material.LILY_PAD, Material.FIRE, Material.DEAD_BUSH,
			Material.MELON_STEM, Material.PUMPKIN_STEM, Material.BROWN_MUSHROOM, Material.RED_MUSHROOM,
			Material.NETHER_WART, Material.REDSTONE_WIRE, Material.COMPARATOR, Material.REPEATER, Material.SLIME_BLOCK,
			Material.STRUCTURE_VOID, Material.SUGAR_CANE, Material.TNT, Material.TRIPWIRE, Material.TRIPWIRE_HOOK,
			Material.WARPED_FUNGUS, Material.CRIMSON_FUNGUS, Material.HONEY_BLOCK, Material.NETHER_SPROUTS,
			Material.CRIMSON_ROOTS, Material.WARPED_ROOTS, Material.TWISTING_VINES_PLANT, Material.WEEPING_VINES_PLANT);

	public SystemManager() {
		for(Material mat : Material.values())
			if(mat.name().contains("POTTED")) excludedMaterials.add(mat);
		excludedMaterials.addAll(Breaker.get().getPackkit().getNMS().getExcludedBlocks());
	}
}
