package com.asangarin.packkit.nms;

import com.asangarin.breaker.network.BlockDigPacketInfo;
import io.netty.channel.Channel;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.MinecraftKey;
import net.minecraft.server.v1_16_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_16_R3.SoundEffect;
import net.minecraft.server.v1_16_R3.TileEntitySkull;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

public class NMSHandler_1_16_R3 extends NMSHandler {
	@Override
	public Channel getChannel(Player player) {
		return ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
	}

	@Override
	public BlockDigPacketInfo readBlockDigPacket(Object packet) {
		if (!(packet instanceof PacketPlayInBlockDig)) return null;
		PacketPlayInBlockDig packetDig = (PacketPlayInBlockDig) packet;
		BlockPosition pos = packetDig.b();
		return new BlockDigPacketInfo(pos.getX(), pos.getY(), pos.getZ(), packetDig.d().name());
	}

	@Override
	public void breakBlock(Player player, Location loc) {
		((CraftPlayer) player).getHandle().playerInteractManager.breakBlock(new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
	}

	@Override
	public Sound getBreakSound(Block block) {
		try {
			net.minecraft.server.v1_16_R3.Block nmsBlock = ((CraftWorld) block.getWorld()).getHandle().getType(new BlockPosition(block.getX(), block.getY(), block.getZ())).getBlock();
			SoundEffect nmsSound = nmsBlock.getStepSound(nmsBlock.getBlockData()).breakSound;

			Field keyField = SoundEffect.class.getDeclaredField(getMapping("keyField"));
			keyField.setAccessible(true);
			MinecraftKey nmsString = (MinecraftKey) keyField.get(nmsSound);

			return Sound.valueOf(nmsString.getKey().replace(".", "_").toUpperCase());
		} catch (IllegalAccessException | NoSuchFieldException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Material> getExcludedBlocks() {
		return Collections.emptyList();
	}

	@Override
	public String getSkullValue(Block block) {
		TileEntitySkull skullTile = (TileEntitySkull) ((CraftWorld) block.getWorld()).getHandle().getTileEntity(new BlockPosition(block.getX(), block.getY(), block.getZ()));
		if (skullTile == null || skullTile.gameProfile == null) return "";
		return skullTile.gameProfile.getProperties().get("textures").iterator().next().getValue();
	}
}
