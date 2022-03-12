package eu.asangarin.packkit.nms;

import eu.asangarin.breaker.network.BlockDigPacketInfo;
import io.netty.channel.Channel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.block.CraftBlock;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class NMSHandler_1_18_R2 implements NMSHandler {
	@Override
	public Channel getChannel(Player player) {
		return ((CraftPlayer) player).getHandle().connection.connection.channel;
	}

	@Override
	public BlockDigPacketInfo readBlockDigPacket(Player player, Object packet) {
		if (!(packet instanceof ServerboundPlayerActionPacket packetDig)) return null;
		BlockPos pos = packetDig.getPos();
		return new BlockDigPacketInfo(player, pos.getX(), pos.getY(), pos.getZ(), packetDig.getAction().name());
	}

	@Override
	public void breakBlock(BlockDigPacketInfo info) {
		Player player = info.getPlayer().get();
		Block block = info.getBlock();
		if (player == null || block == null) return;
		block.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation().add(0.5, 0.5, 0.5), 100, 0.1, 0.1, 0.1, 4, block.getBlockData());
		block.getWorld().playSound(block.getLocation(), block.getBlockData().getSoundGroup().getBreakSound(), 1.0f, 1.0f);
		((CraftPlayer) player).getHandle().gameMode.destroyBlock(new BlockPos(block.getX(), block.getY(), block.getZ()));
	}

	@Override
	public List<Material> getExcludedBlocks() {
		return Arrays.asList(Material.SMALL_DRIPLEAF, Material.CAVE_VINES_PLANT, Material.CAVE_VINES, Material.SPORE_BLOSSOM, Material.AZALEA,
				Material.FLOWERING_AZALEA);
	}

	@Override
	public String getSkullValue(Block block) {
		SkullBlockEntity skullTile = (SkullBlockEntity) ((CraftWorld) block.getWorld()).getHandle()
				.getBlockEntity(new BlockPos(block.getX(), block.getY(), block.getZ()));
		if (skullTile == null || skullTile.getOwnerProfile() == null) return "";
		return skullTile.getOwnerProfile().getProperties().get("textures").iterator().next().getValue();
	}

	@Override
	public Object createDigAnimationPacket(BlockDigPacketInfo info, int stage) {
		return new ClientboundBlockDestructionPacket(info.getEntityId(), new BlockPos(info.getX(), info.getY(), info.getZ()), stage);
	}

	@Override
	public Collection<String> getTagsFor(Block block) {
		List<String> tags = new ArrayList<>();

		((CraftBlock) block).getNMS().getTags().forEach((tag) -> {
			ResourceLocation location = tag.location();
			if (location.getNamespace().equals("minecraft")) tags.add("#" + location.getPath());
			tags.add("#" + location.getNamespace() + ":" + location.getPath());
		});

		return tags;
	}
}
