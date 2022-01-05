package eu.asangarin.breaker.api;

import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

@Getter
public class BreakerTriggerEvent extends PlayerEvent {
	private static final HandlerList HANDLERS = new HandlerList();

	private final Block block;
	private final String args;

	public BreakerTriggerEvent(Player player, Block block, String args) {
		super(player);
		this.block = block;
		this.args = args;
	}

	@Override
	public @NotNull
	HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
