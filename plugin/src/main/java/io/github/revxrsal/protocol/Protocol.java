package io.github.revxrsal.protocol;

import eu.asangarin.packkit.nms.NMSHandler;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;

/** A simple utility for dealing with protocol-specific code
 * - by ReflxctionDev - https://github.com/ReflxctionDev */
public class Protocol {
	/** The server version, e.g "v1_16_R3" */
	public static final String VERSION = getVersion();

	/** Returns the server protocol version, e.g v1_16_R3
	 @return The server version */
	private static String getVersion() {
		final String packageName = Bukkit.getServer().getClass().getPackage().getName();
		return packageName.substring(packageName.lastIndexOf('.') + 1);
	}

	public static NMSHandler getNMSHandler() {
		try {
			return (NMSHandler) Class.forName("eu.asangarin.packkit.nms.NMSHandler_" + VERSION.substring(1)).getDeclaredConstructor().newInstance();
		} catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException _ignored) {
			return null;
		}
	}
}