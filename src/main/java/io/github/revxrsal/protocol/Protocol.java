package io.github.revxrsal.protocol;

import com.asangarin.packkit.nms.NMSHandler;
import com.asangarin.packkit.nms.NMSHandler_1_16_R3;
import org.bukkit.Bukkit;

/**
 * A simple utility for dealing with protocol-specific code
 * - by ReflxctionDev - https://github.com/ReflxctionDev
 */
public class Protocol {
	/**
	 * The server version, e.g "v1_16_R3"
	 */
	public static final String VERSION = getVersion();

	/**
	 * Returns the server protocol version, e.g v1_16_R3
	 *
	 * @return The server version
	 */
	private static String getVersion() {
		final String packageName = Bukkit.getServer().getClass().getPackage().getName();
		return packageName.substring(packageName.lastIndexOf('.') + 1);
	}

	public static NMSHandler getNMSHandler() {
		return new NMSHandler_1_16_R3();
	}
}