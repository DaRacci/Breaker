package com.asangarin.packkit;

import com.asangarin.packkit.nms.NMSHandler;
import io.github.revxrsal.protocol.Protocol;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Packkit implements Listener {
	private final NMSHandler nms;
	private final NetworkHandler handler;

	public Packkit(NetworkHandler handler) {
		this.handler = handler;
		this.nms = Protocol.getNMSHandler();
	}

	@EventHandler
	private void onJoin(PlayerJoinEvent event) {
		inject(event.getPlayer());
	}

	@EventHandler
	private void onQuit(PlayerQuitEvent event) {
		close(event.getPlayer());
	}

	public void inject(Player player) {
		ChannelDuplexHandler duplexHandler = new ChannelDuplexHandler() {
			@Override
			public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
				if (handler.readBefore(player, packet) == PacketStatus.DENY) return;
				super.channelRead(ctx, packet);
				handler.readAfter(player, packet);
			}

			@Override
			public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception {
				if (handler.writeBefore(player, packet) == PacketStatus.DENY) return;
				super.write(ctx, packet, promise);
				handler.writeAfter(player, packet);
			}
		};

		nms.getChannel(player).pipeline().addBefore("packet_handler", player.getUniqueId().toString(), duplexHandler);
	}

	public void close(Player player) {
		Channel channel = nms.getChannel(player);
		channel.eventLoop().submit(() -> {
			channel.pipeline().remove(player.getUniqueId().toString());
			return null;
		});
	}

	public void sendPacket(Player player, Object packet) {
		nms.getChannel(player).pipeline().writeAndFlush(packet);
	}

	public NMSHandler getNMS() {
		return nms;
	}
}
