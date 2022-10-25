package eu.asangarin.breaker.system;

import eu.asangarin.breaker.Breaker;
import eu.asangarin.breaker.api.BreakerState;
import eu.asangarin.breaker.comp.mythicmobs.MythicMobsCompat;
import eu.asangarin.breaker.network.BlockDigPacketInfo;
import eu.asangarin.breaker.util.BlockFile;
import eu.asangarin.breaker.util.ToolCalc;
import eu.asangarin.breaker.util.TriggerType;
import io.lumine.mythic.api.skills.Skill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.utils.config.LineConfig;
import io.lumine.mythic.bukkit.utils.config.properties.Property;
import io.lumine.mythic.core.skills.variables.VariableRegistry;
import io.lumine.mythic.core.skills.variables.VariableScope;
import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
public class DatabaseBlock {
	private final int min, max, base;
	private final boolean vanillaEnabled, tools, efficiency, haste, water, air;
	private final List<BreakerState> states = new ArrayList<>();
	private final Map<TriggerType, List<BreakerTrigger.TriggerTrigger>> triggers = BreakerTrigger.newTriggerMap();

	private final String mythicSkill, mythicVariable;
	private final VariableScope mythicScope;

	public DatabaseBlock(BlockFile file) {
		this.max = Property.Int(file, "hardness.max", 40).get();
		this.min = Property.Int(file, "hardness.min", 20).get();
		this.base = Property.Int(file, "hardness.base", max).get();

		this.vanillaEnabled = Property.Boolean(file, "use-modifiers.vanilla.enabled", false).get();
		this.tools = Property.Boolean(file, "use-modifiers.vanilla.tools", false).get();
		this.efficiency = Property.Boolean(file, "use-modifiers.vanilla.efficiency", false).get();
		this.haste = Property.Boolean(file, "use-modifiers.vanilla.haste", false).get();
		this.water = Property.Boolean(file, "use-modifiers.vanilla.water", false).get();
		this.air = Property.Boolean(file, "use-modifiers.vanilla.air", false).get();

		this.mythicSkill = Property.String(file, "hardness.mythic.skill", "").get();
		this.mythicVariable = Property.String(file, "hardness.mythic.variable", "").get();

		String strScope = Property.String(file, "hardness.mythic.scope", VariableScope.SKILL.toString()).get();
		VariableScope scope;
		try {
			scope = VariableScope.valueOf(strScope.toUpperCase());
		} catch (IllegalArgumentException ex) {
			scope = VariableScope.SKILL;
		}
		this.mythicScope = scope;

		for (String conf : Property.StringList(file, "states").get())
			Breaker.get().getBreakerStates().fromConfig(file.get().substring(7), conf).ifPresent(states::add);
		for (String conf : Property.StringList(file, "triggers").get())
			fromConfig(file.get().substring(7), conf).ifPresent(trigger -> triggers.get(trigger.getType()).add(trigger.prepare()));
	}

	private Optional<BreakerTrigger> fromConfig(String name, String input) {
		BreakerTrigger trigger = new BreakerTrigger();
		if (trigger.validate(name, new LineConfig(input))) return Optional.of(trigger);
		return Optional.empty();
	}

	public int calculateBreakTime(BlockDigPacketInfo info) {
		Player player = info.getPlayer().get();
		Block block = info.getBlock();
		if (player == null || block == null) return -1;

		int breakTime = base;
		//System.out.println("Start Break Time: " + breakTime);
		//System.out.println("Tool: " + tools + " - Efficiency: " + efficiency + " - Haste: " + haste + " - Water: " + water + " - Air: " + air);
		if (mythicSkill.isEmpty() || mythicVariable.isEmpty()) {
			for (BreakerState state : states)
				if (state.isConditionMet(player, block)) breakTime -= state.getDeduction(player, block);
			//System.out.println("After State Break Time: " + breakTime);

			if(vanillaEnabled) {
				double multiplier = 1;
				ItemStack hand = player.getInventory().getItemInMainHand();
				if (tools && block.isPreferredTool(hand)) {
					//System.out.println("TOOL: " + hand.getType() + " | BLOCK: " + block.getType());
					multiplier = ToolCalc.getToolMultiplier(hand.getType(), block.getType());
					//System.out.println("Tool Multiplier: " + multiplier);

					if (efficiency && hand.getEnchantments().containsKey(Enchantment.DIG_SPEED)) multiplier += hand.getEnchantments().get(Enchantment.DIG_SPEED) ^ 2 + 1;

					//System.out.println("Post-Enchant Multiplier: " + multiplier);
				}

				if (!tools && efficiency && hand.getEnchantments().containsKey(Enchantment.DIG_SPEED)) multiplier += hand.getEnchantments().get(Enchantment.DIG_SPEED) ^ 2 + 1;

				if (haste && player.hasPotionEffect(PotionEffectType.FAST_DIGGING)) multiplier *= 0.2 * player.getPotionEffect(PotionEffectType.FAST_DIGGING).getAmplifier() + 1;
				//System.out.println("Post-Effect Multiplier: " + multiplier);

				if (water && player.isInWater() && !hand.getEnchantments().containsKey(Enchantment.WATER_WORKER)) multiplier /= 5;
				//System.out.println("Post-Water Multiplier: " + multiplier);

				if (air && !player.isOnGround()) multiplier /= 5;
				//System.out.println("Post-Water Multiplier: " + multiplier);

				double newBreakTime = (((double) breakTime / 20) * 1.5);
				double damage = multiplier / newBreakTime;
				//System.out.println("Break Time Calc: " + breakTime + " | " + newBreakTime);
				//System.out.println("Damage Calculation: " + damage + " ( " + multiplier + " | " + newBreakTime + ")");

				if (tools && block.isPreferredTool(hand)) damage /= 30;
				else damage /= 100;
				//System.out.println("Post Damage: " + damage);

				breakTime = (damage > 1) ? 1 : (int) Math.ceil(1 / damage);
				//System.out.println("After Vanilla Break Time: " + breakTime);
			}
		} else {
			Optional<Skill> optionalSkill = MythicBukkit.inst().getSkillManager().getSkill(mythicSkill);
			if (optionalSkill.isPresent()) {
				SkillMetadata meta = MythicMobsCompat.createMeta(player, block.getLocation().add(0.5, 0.5, 0.5));
				optionalSkill.get().execute(meta);
			} else {
				Breaker.error("Tried running a MythicMobs skill, but '" + mythicSkill + "' is not a valid skill!");
			}
			final VariableRegistry registry = MythicBukkit.inst().getVariableManager().getRegistry(mythicScope, BukkitAdapter.adapt(player));
			breakTime = registry.getInt(mythicVariable);
		}

		return Math.min(max, Math.max(min, breakTime));
	}

	public boolean canMine(BlockDigPacketInfo info) {
		Player player = info.getPlayer().get();
		Block block = info.getBlock();

		for (BreakerState state : states)
			if (state.isRequired() && !state.isConditionMet(player, block)) return false;

		return true;
	}

	@Override
	public String toString() {
		return String.format("[%d, %d, %d]", max, min, base);
	}

	public void trigger(TriggerType start, BlockDigPacketInfo info) {
		Player player = info.getPlayer().get();
		if (player == null) return;

		triggers.get(start).forEach(t -> t.trigger(player, info.getBlock()));
	}
}
