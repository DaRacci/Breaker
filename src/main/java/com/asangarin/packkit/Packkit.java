package com.asangarin.packkit;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;

@Getter
public class Packkit implements Listener {
	private final NetworkHandler handler;

	public Packkit(NetworkHandler handler) {
		this.handler = handler;
	}

	@EventHandler
	private void inject(PlayerJoinEvent event) {
		ChannelDuplexHandler duplexHandler = new ChannelDuplexHandler() {
			@Override
			public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
				PacketStatus status = handler.readBefore(packet);
				if(status == PacketStatus.DENY) return;
				super.channelRead(ctx, packet);
				handler.readAfter(packet);
			}

			@Override
			public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception {
				PacketStatus status = handler.writeBefore(packet);
				if(status == PacketStatus.DENY) return;
				super.write(ctx, packet, promise);
				handler.writeAfter(packet);
			}
		};

		ChannelPipeline pipeline = ((CraftPlayer) event.getPlayer()).getHandle().playerConnection.networkManager.channel.pipeline();
		pipeline.addBefore("packet_handler", event.getPlayer().getUniqueId().toString(), duplexHandler);
	}

	@EventHandler
	private void eject(PlayerQuitEvent event) {
		Channel channel = ((CraftPlayer) event.getPlayer()).getHandle().playerConnection.networkManager.channel;
		channel.eventLoop().submit(() -> {
			channel.pipeline().remove(event.getPlayer().getUniqueId().toString());
			return null;
		});
	}
}
