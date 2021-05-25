package com.asangarin.breaker.system;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.network.BlockDigPacketInfo;
import io.lumine.mythic.utils.Schedulers;
import io.lumine.mythic.utils.tasks.Task;
import lombok.Getter;

@Getter
public class BreakerBlock {
	private final DatabaseBlock block;
	private final BlockDigPacketInfo info;
	private final Task task;

	public BreakerBlock(BlockDigPacketInfo info, DatabaseBlock block) {
		this.info = info;
		this.block = block;
		task = Schedulers.async().runRepeating(this::run, 0, block.calculateBreakTime(info));
	}

	private void run(Task task) {
		Breaker.get().getBreakingSystem().sendBreakAnimationPacket(info, task.getTimesRan());
		if (task.getTimesRan() == 10) breakBlock();
	}

	public void stop() {
		Breaker.get().getBreakingSystem().sendBreakAnimationPacket(info, 10);
		task.close();
	}

	public void breakBlock() {
		stop();
		Schedulers.sync().run(() -> Breaker.get().getPackkit().getNMS().breakBlock(info));
	}
}
