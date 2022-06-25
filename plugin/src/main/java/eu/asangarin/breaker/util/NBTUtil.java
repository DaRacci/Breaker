package eu.asangarin.breaker.util;

import io.lumine.mythic.api.volatilecode.handlers.VolatileItemHandler;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.utils.jnbt.CompoundTag;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class NBTUtil {
	public static CompoundTag findDeepestTag(String[] nodes, ItemStack stack) {
		VolatileItemHandler handler = MythicBukkit.inst().getVolatileCodeHandler().getItemHandler();
		CompoundTag tag = handler.getNBTData(stack);

		for (int i = 0; i < nodes.length; i++) {
			if (i + 1 == nodes.length) return tag;

			Optional<CompoundTag> nextTag = navigate(tag, nodes[i]);
			if (nextTag.isPresent()) tag = nextTag.get();
			else return tag;
		}

		return tag;
	}

	public static int getIntValue(String key, ItemStack stack) {
		String[] nodes = key.split("\\.");
		String finalKey = nodes[nodes.length - 1];
		CompoundTag tag = findDeepestTag(nodes, stack);
		return tag.getInt(finalKey);
	}

	public static String getStringValue(String key, ItemStack stack) {
		String[] nodes = key.split("\\.");
		String finalKey = nodes[nodes.length - 1];
		CompoundTag tag = findDeepestTag(nodes, stack);
		return tag.getString(finalKey);
	}

	public static boolean getBooleanValue(String key, ItemStack stack) {
		String[] nodes = key.split("\\.");
		String finalKey = nodes[nodes.length - 1];
		CompoundTag tag = findDeepestTag(nodes, stack);
		return tag.getBoolean(finalKey);
	}

	private static Optional<CompoundTag> navigate(CompoundTag tag, String key) {
		return Optional.ofNullable(tag.getCompound(key));
	}
}
