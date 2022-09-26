package eu.asangarin.breaker.system;

import eu.asangarin.breaker.Breaker;
import eu.asangarin.breaker.network.BlockDigPacketInfo;
import eu.asangarin.breaker.util.TriggerType;
import io.lumine.mythic.bukkit.utils.Schedulers;
import io.lumine.mythic.bukkit.utils.tasks.Task;
import lombok.Getter;
import org.bukkit.Location;

public class ActiveBlock {
	private final Task task;
	private final int breakTime;

	@Getter
	private final BlockDigPacketInfo info;
	private final DatabaseBlock block;

	public ActiveBlock(BlockDigPacketInfo info, DatabaseBlock block, int breakTime) {
		this.info = info;
		this.block = block;
		this.breakTime = breakTime;

		Schedulers.sync().run(() -> block.trigger(TriggerType.START, info));
		task = Schedulers.async().runRepeating(this::run, 0, 1);
	}

	private void run(Task task) {
		if (info.getPlayer() == null || !info.getPlayer().get().isOnline()) {
			forceAbort();
			return;
		}
		Schedulers.sync().run(() -> Breaker.get().getBreakingSystem().sendBreakAnimationPacket(info, getProgress()));
		if (task.getTimesRan() == breakTime) breakBlock();
	}

	private int getProgress() {
		return (int) Math.floor(((double) (task.getTimesRan() + 1) / (double) breakTime) * 10);
	}

	private void forceAbort() {
		task.close();
		Schedulers.sync().run(() -> Breaker.get().getBreakingSystem().sendBreakAnimationPacket(info, 10));
	}

	public void abort() {
		task.close();
		Schedulers.sync().run(() -> {
			Breaker.get().getBreakingSystem().sendBreakAnimationPacket(info, 10);
			block.trigger(TriggerType.ABORT, info);
			block.trigger(TriggerType.STOP, info);
		});
	}

	public void breakBlock() {
		task.close();
		Schedulers.sync().run(() -> {
			Breaker.get().getBreakingSystem().sendBreakAnimationPacket(info, 10);
			block.trigger(TriggerType.BREAK, info);
			block.trigger(TriggerType.STOP, info);
			Breaker.get().getPackkit().getNMS().breakBlock(info);
		});
	}

	public boolean compareLocation(Location loc) {
		return info.compareLocation(loc);
	}
}
