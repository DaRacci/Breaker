package com.asangarin.breaker.nms;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.asangarin.breaker.utility.NMSHandler;

import net.minecraft.server.v1_12_R1.Block;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.SoundEffect;
import net.minecraft.server.v1_12_R1.SoundEffectType;
import net.minecraft.server.v1_12_R1.World;


public class NMSHandler_1_12_R1 extends NMSHandler
{
	public List<Material> excludedMaterials = new ArrayList<Material>(
			Arrays.asList(Material.AIR, Material.GRASS, Material.END_ROD, Material.BARRIER, Material.TORCH, Material.getMaterial("REDSTONE_TORCH_ON"),
			Material.getMaterial("REDSTONE_TORCH_OFF"), Material.getMaterial("LONG_GRASS"), Material.getMaterial("BEETROOT_BLOCK"), Material.WHEAT,
			Material.POTATO, Material.CARROT, Material.getMaterial("SAPLING"), Material.FLOWER_POT, Material.getMaterial("YELLOW_FLOWER"),
			Material.getMaterial("RED_ROSE"), Material.getMaterial("DOUBLE_PLANT"), Material.getMaterial("WATER_LILY"), Material.FIRE, Material.DEAD_BUSH,
			Material.MELON_STEM, Material.PUMPKIN_STEM, Material.BROWN_MUSHROOM, Material.RED_MUSHROOM, Material.NETHER_WART_BLOCK, Material.REDSTONE_WIRE,
			Material.getMaterial("REDSTONE_COMPARATOR_OFF"), Material.getMaterial("REDSTONE_COMPARATOR_ON"), Material.SLIME_BLOCK, Material.getMaterial("DIODE_BLOCK_OFF"),
			Material.getMaterial("DIODE_BLOCK_ON"), Material.STRUCTURE_VOID, Material.getMaterial("SUGAR_CANE_BLOCK"), Material.TNT, Material.TRIPWIRE, Material.TRIPWIRE_HOOK));
	
	@Override
	public String getVersion()
	{ return "1.12_R1"; }
	
	@Override
	public void breakBlock(Player player, Location loc) {
		((CraftPlayer) player).getHandle().playerInteractManager.breakBlock(new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
	}

	@Override
	public Sound getBlockBreakSound(org.bukkit.block.Block block) {
		try {
            World nmsWorld = ((CraftWorld) block.getWorld()).getHandle();

            Block nmsBlock = nmsWorld.getType(new BlockPosition(block.getX(), block.getY(), block.getZ())).getBlock();
            SoundEffectType soundEffectType = nmsBlock.getStepSound();

            Field breakSound = SoundEffectType.class.getDeclaredField("o");
            breakSound.setAccessible(true);
            SoundEffect nmsSound = (SoundEffect) breakSound.get(soundEffectType);

            Field keyField = SoundEffect.class.getDeclaredField("b");
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
		return excludedMaterials;
	}
}
