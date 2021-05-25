package com.asangarin.packkit.nms;

import com.asangarin.breaker.network.BlockDigPacketInfo;
import io.netty.channel.Channel;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_16_R3.PacketPlayOutBlockBreakAnimation;
import net.minecraft.server.v1_16_R3.TileEntitySkull;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class NMSHandler_1_16_R3 implements NMSHandler {
	@Override
	public Channel getChannel(Player player) {
		return ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
	}

	@Override
	public BlockDigPacketInfo readBlockDigPacket(Player player, Object packet) {
		if (!(packet instanceof PacketPlayInBlockDig)) return null;
		PacketPlayInBlockDig packetDig = (PacketPlayInBlockDig) packet;
		BlockPosition pos = packetDig.b();
		return new BlockDigPacketInfo(player, pos.getX(), pos.getY(), pos.getZ(), packetDig.d().name());
	}

	@Override
	public void breakBlock(BlockDigPacketInfo info) {
		Player player = info.getPlayer().get();
		Block block = info.getBlock();
		if(player == null || block == null) return;
		block.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation().add(0.5, 0.5, 0.5), 100, 0.1, 0.1, 0.1, 4, block.getBlockData());
		block.getWorld().playSound(block.getLocation(), block.getBlockData().getSoundGroup().getBreakSound(), 1.0f, 1.0f);
		((CraftPlayer) player).getHandle().playerInteractManager.breakBlock(new BlockPosition(block.getX(), block.getY(), block.getZ()));
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

	@Override
	public Object createDigAnimationPacket(BlockDigPacketInfo info, int stage) {
		return new PacketPlayOutBlockBreakAnimation(info.getEntityId(), new BlockPosition(info.getX(), info.getY(), info.getZ()), stage);
	}
}
