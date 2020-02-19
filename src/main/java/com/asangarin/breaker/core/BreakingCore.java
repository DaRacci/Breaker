package com.asangarin.breaker.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.utility.BreakTrigger;
import com.asangarin.breaker.utility.BreakerSystem;
import com.asangarin.breaker.utility.TriggerType;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;

public class BreakingCore {
	HashMap<Integer, BreakingBlock> cachedBlocks = new HashMap<Integer, BreakingBlock>();
	private List<Material> excludedMaterials = Breaker.plugin.nms.getExlcudedBlocks();
	private List<BreakerSystem> systems = new ArrayList<>();
	
	public BreakingCore() {
		for(Class<? extends BreakerSystem> breakerSystems : Breaker.plugin.system.registered())
			try { systems.add(breakerSystems.newInstance()); } catch (InstantiationException | IllegalAccessException e) { e.printStackTrace(); }
		systems = systems.stream().sorted(Comparator.comparingInt(BreakerSystem::priority)).collect(Collectors.toList());
		
        //final Thread mainThread = Thread.currentThread();
		Breaker.plugin.protocol.getAsynchronousManager().registerAsyncHandler
		(new PacketAdapter(Breaker.plugin, ListenerPriority.HIGHEST, PacketType.Play.Client.BLOCK_DIG) {            
			@Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                EnumWrappers.PlayerDigType digType = packet.getPlayerDigTypes().getValues().get(0);
                
                if(event.getPacketType() != PacketType.Play.Client.BLOCK_DIG) return;
                Player player = event.getPlayer();
                if(player.getGameMode().equals(GameMode.CREATIVE)) return;
                BlockPosition bp = packet.getBlockPositionModifier().read(0);
                Block block = player.getWorld().getBlockAt(bp.getX(), bp.getY(), bp.getZ());
                if(block == null) return;
                
                if(digType != EnumWrappers.PlayerDigType.START_DESTROY_BLOCK)
                	event.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
                
                if(excludedMaterials.contains(block.getType())) return;
                
                BreakerSystem system = null;
                
                for(BreakerSystem sys : systems) {
                	if(Breaker.plugin.database.has(sys.getId(block)))
                	{ system = sys; break; }
                }
                
                if(system != null) {
                    BreakingBlock breakingBlock;

                    if(digType == EnumWrappers.PlayerDigType.START_DESTROY_BLOCK) {
                    	breakingBlock = new BreakingBlock(system.getId(block), block, player);
                    	cachedBlocks.put(getBlockEntityId(block), breakingBlock);
                    	breakingBlock.start();
                    }
                    else {
                    	int blockId = getBlockEntityId(block);
                    	
                    	if(cachedBlocks.containsKey(blockId)) {
                        	breakingBlock = cachedBlocks.remove(blockId);
                        	breakingBlock.cancel();
                        	for(BreakTrigger trigger : breakingBlock.getBlockConfiguration().getTriggers(TriggerType.ABORT))
                        		trigger.execute(breakingBlock.getBreaker(), breakingBlock.getBlock());
                    	}
                    }
                }
            }
        }).syncStart();
    }

	public boolean contains(Block block) {
		return cachedBlocks.containsKey(getBlockEntityId(block));
	}
	public void caught(Block block) {
		cachedBlocks.remove(getBlockEntityId(block)).cancel();
	}
	
	public static int getBlockEntityId(Block block) {
		return ((block.getX() & 0xFFF) << 20) | ((block.getZ() & 0xFFF) << 8) | (block.getY() & 0xFF);
	}
	
	public List<BreakerSystem> getActiveSystems()
	{ return systems; }
}
