package eu.asangarin.breaker.util;

import org.bukkit.Material;

public class ToolCalc {
	public static double getToolMultiplier(Material type, Material block) {
		return switch (type) {
			case WOODEN_SWORD, GOLDEN_SWORD, STONE_SWORD, IRON_SWORD, DIAMOND_SWORD, NETHERITE_SWORD -> block == Material.COBWEB ? 15 : 1.5;

			case WOODEN_AXE, WOODEN_HOE, WOODEN_SHOVEL, WOODEN_PICKAXE -> 2;
			case GOLDEN_AXE, GOLDEN_HOE, GOLDEN_SHOVEL, GOLDEN_PICKAXE -> 12;
			case STONE_AXE, STONE_HOE, STONE_SHOVEL, STONE_PICKAXE -> 4;
			case IRON_AXE, IRON_HOE, IRON_SHOVEL, IRON_PICKAXE -> 6;
			case DIAMOND_AXE, DIAMOND_HOE, DIAMOND_SHOVEL, DIAMOND_PICKAXE -> 8;
			case NETHERITE_AXE, NETHERITE_HOE, NETHERITE_SHOVEL, NETHERITE_PICKAXE -> 9;

			case SHEARS -> calculateShears(block);

			default -> 1;
		};
	}

	public static double calculateShears(Material block) {
		return switch (block) {
			case VINE, CAVE_VINES, GLOW_LICHEN -> 1;
			case WHITE_WOOL, BLACK_WOOL, BLUE_WOOL, BROWN_WOOL, CYAN_WOOL, GRAY_WOOL, GREEN_WOOL, LIGHT_BLUE_WOOL,
					LIGHT_GRAY_WOOL, LIME_WOOL, MAGENTA_WOOL, ORANGE_WOOL, PINK_WOOL, PURPLE_WOOL, RED_WOOL, YELLOW_WOOL ->
					5;
			case COBWEB, ACACIA_LEAVES, AZALEA_LEAVES, BIRCH_LEAVES, DARK_OAK_LEAVES,
					FLOWERING_AZALEA_LEAVES, JUNGLE_LEAVES, MANGROVE_LEAVES, OAK_LEAVES, SPRUCE_LEAVES ->
					15;
			default -> 2;
		};
	}
}
