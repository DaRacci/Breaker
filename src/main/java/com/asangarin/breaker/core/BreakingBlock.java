package com.asangarin.breaker.core;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.Settings;
import com.asangarin.breaker.api.BreakState;
import com.asangarin.breaker.api.BreakTrigger;
import com.asangarin.breaker.api.TriggerType;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;

public class BreakingBlock {
	private Block block;
	private Player breaker;
	private BlockConfiguration blockConfig;
	
	BukkitRunnable runnable;

	public BreakingBlock(String id, Block block, Player breaker) {
		this.blockConfig = Breaker.plugin.database.get(id);
		this.block = block;
		this.breaker = breaker;
	}

	public void start() {
        for(BreakTrigger trigger : blockConfig.getTriggers(TriggerType.START))
        		trigger.execute(breaker, block);

        runnable = new BukkitRunnable() {
            int stage = 0;
            
        	@Override
        	public void run() {
        		//Send the block break animation depending on stage of breaking.
                PacketContainer customAnimation = Breaker.plugin.protocol.createPacket(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
                customAnimation.getBlockPositionModifier().write(0, new BlockPosition(block.getX(), block.getY(), block.getZ()));
                customAnimation.getIntegers().write(0, BreakingCore.getBlockEntityId(block));
                customAnimation.getIntegers().write(1, stage);
                
                try {
					Breaker.plugin.protocol.sendServerPacket(breaker, customAnimation);
				} catch (InvocationTargetException e) {
				    throw new RuntimeException("Cannot send packet " + customAnimation, e);
				}
                
        		stage += 1;
        		for(BreakTrigger trigger : blockConfig.getTriggers(TriggerType.MINE))
        			trigger.execute(breaker, block);
        		
        		if(stage == 5)
            		for(BreakTrigger trigger : blockConfig.getTriggers(TriggerType.HALFWAY))
            			trigger.execute(breaker, block);
        		else if(stage > 10) {
        			//One it reaches stage 10 break the block.
        			breaker.playSound(block.getLocation(), Breaker.plugin.nms.getBlockBreakSound(block), 1.0f, 1.0f);
        			Breaker.plugin.legacy.spawnBlockParticle(block);
        			Breaker.plugin.nms.breakBlock(breaker, block.getLocation());
        	    	for(BreakTrigger trigger : blockConfig.getTriggers(TriggerType.FINISH))
        	    		trigger.execute(breaker, block);
        			finish();
        			cancel();
        		}
        	}
        };
        
        runnable.runTaskTimer(Breaker.plugin, 0, calculateBreakTime() / 10);
	}
	
	public void cancel() {
		if(runnable != null)
    		runnable.cancel();
		finish();
	}
	
	public void finish() {
        PacketContainer cancelBlockBreak = Breaker.plugin.protocol.createPacket(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
        if(cancelBlockBreak == null) return;
        
        cancelBlockBreak.getBlockPositionModifier().write(0, new BlockPosition(block.getX(), block.getY(), block.getZ()));
        cancelBlockBreak.getIntegers().write(0, BreakingCore.getBlockEntityId(block));
        cancelBlockBreak.getIntegers().write(1, 10);
		
        try {
			Breaker.plugin.protocol.sendServerPacket(breaker, cancelBlockBreak);
		} catch (InvocationTargetException e) {
		    throw new RuntimeException("Cannot send packet " + cancelBlockBreak, e);
		}
        
    	if(breaker.hasPotionEffect(PotionEffectType.SLOW_DIGGING))
    		new BukkitRunnable() {
    			@Override
    			public void run() {
    				if(!Settings.instance().permanentMiningFatigue()) breaker.removePotionEffect(PotionEffectType.SLOW_DIGGING);
    			}
        	}.runTask(Breaker.plugin);
        	

    	for(BreakTrigger trigger : blockConfig.getTriggers(TriggerType.STOP))
    		trigger.execute(breaker, block);
	}
	
	public BlockConfiguration getBlockConfiguration()
	{ return blockConfig; }
	public Block getBlock()
	{ return block; }
	public Player getBreaker()
	{ return breaker; }

	public int calculateBreakTime() {
		int breakingTime = blockConfig.getMaxHardness();
		
		for(BreakState state : blockConfig.getStates()) {
			Breaker.debug("Found state! " + state, 20);
			if(state.activeState(this))
				breakingTime -= state.getStateValue(this);
		}
		
		breakingTime = Math.max(breakingTime, blockConfig.getMinHardness());
		Breaker.debug("Breaking Time: " + breakingTime, 1);
		return breakingTime;
	}
}