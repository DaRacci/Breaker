package com.asangarin.packkit;

public interface NetworkHandler {
	default void readAfter(Object packet) {};
	default void writeAfter(Object packet) {};
	default PacketStatus readBefore(Object packet) { return PacketStatus.ALLOW; };
	default PacketStatus writeBefore(Object packet) { return PacketStatus.ALLOW; };
}
