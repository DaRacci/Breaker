package com.asangarin.breaker.nms;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.asangarin.breaker.api.NMSHandler;

import net.minecraft.server.v1_14_R1.Block;
import net.minecraft.server.v1_14_R1.BlockPosition;
import net.minecraft.server.v1_14_R1.MinecraftKey;
import net.minecraft.server.v1_14_R1.SoundEffect;
import net.minecraft.server.v1_14_R1.SoundEffectType;
import net.minecraft.server.v1_14_R1.World;

public class NMSHandler_1_14_R1 extends NMSHandler
{
	public List<Material> excludedMaterials = new ArrayList<Material>(
			Arrays.asList(Material.AIR, Material.GRASS, Material.TALL_GRASS, Material.END_ROD, Material.BARRIER, Material.BRAIN_CORAL,
			Material.BRAIN_CORAL_FAN, Material.BUBBLE_CORAL, Material.BUBBLE_CORAL_FAN, Material.FIRE_CORAL, Material.FIRE_CORAL_FAN,
			Material.HORN_CORAL, Material.HORN_CORAL_FAN, Material.TUBE_CORAL, Material.TUBE_CORAL_FAN, Material.DEAD_BRAIN_CORAL,
			Material.DEAD_BRAIN_CORAL_FAN, Material.DEAD_BUBBLE_CORAL, Material.DEAD_BUBBLE_CORAL_FAN, Material.DEAD_FIRE_CORAL,
			Material.DEAD_FIRE_CORAL_FAN, Material.DEAD_HORN_CORAL, Material.DEAD_HORN_CORAL_FAN, Material.DEAD_TUBE_CORAL,
			Material.DEAD_TUBE_CORAL_FAN, Material.TORCH, Material.REDSTONE_TORCH, Material.WALL_TORCH, Material.REDSTONE_WALL_TORCH,
			Material.FERN, Material.LARGE_FERN, Material.BEETROOTS, Material.WHEAT, Material.POTATOES, Material.CARROTS,
			Material.OAK_SAPLING, Material.DARK_OAK_SAPLING, Material.SPRUCE_SAPLING, Material.ACACIA_SAPLING, Material.BIRCH_SAPLING,
			Material.JUNGLE_SAPLING, Material.FLOWER_POT, Material.POPPY, Material.DANDELION, Material.ALLIUM, Material.BLUE_ORCHID,
			Material.AZURE_BLUET, Material.RED_TULIP, Material.ORANGE_TULIP, Material.WHITE_TULIP, Material.PINK_TULIP, Material.OXEYE_DAISY,
			Material.CORNFLOWER, Material.LILY_OF_THE_VALLEY, Material.WITHER_ROSE, Material.SUNFLOWER, Material.LILAC, Material.ROSE_BUSH,
			Material.PEONY, Material.LILY_PAD, Material.FIRE, Material.DEAD_BUSH, Material.MELON_STEM, Material.PUMPKIN_STEM, Material.BROWN_MUSHROOM,
			Material.RED_MUSHROOM, Material.NETHER_WART, Material.REDSTONE_WIRE, Material.COMPARATOR, Material.REPEATER, Material.SLIME_BLOCK,
			Material.STRUCTURE_VOID, Material.SUGAR_CANE, Material.TNT, Material.TRIPWIRE, Material.TRIPWIRE_HOOK));

	@Override
	public String getVersion()
	{ return "1.14_R1"; }

	@Override
	public void breakBlock(Player player, Location loc) {
		((CraftPlayer) player).getHandle().playerInteractManager.breakBlock(new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
	}

	@Override
	public Sound getBlockBreakSound(org.bukkit.block.Block block) {
		try {
            World nmsWorld = ((CraftWorld) block.getWorld()).getHandle();

            Block nmsBlock = nmsWorld.getType(new BlockPosition(block.getX(), block.getY(), block.getZ())).getBlock();
            SoundEffectType soundEffectType = nmsBlock.getStepSound(nmsBlock.getBlockData());

            Field breakSound = SoundEffectType.class.getDeclaredField("y");
            breakSound.setAccessible(true);
            SoundEffect nmsSound = (SoundEffect) breakSound.get(soundEffectType);

            Field keyField = SoundEffect.class.getDeclaredField("a");
            keyField.setAccessible(true);
            MinecraftKey nmsString = (MinecraftKey) keyField.get(nmsSound);

            return Sound.valueOf(nmsString.getKey().replace(".", "_").toUpperCase());
        } catch (IllegalAccessException | NoSuchFieldException ex) {
            ex.printStackTrace();
        }
        return null;
	}

	@Override
	public List<Material> getExlcudedBlocks() {
		for(Material m : Material.values())
			if(m.name().contains("POTTED"))
				excludedMaterials.add(m);
		
		return excludedMaterials;
	}
}
