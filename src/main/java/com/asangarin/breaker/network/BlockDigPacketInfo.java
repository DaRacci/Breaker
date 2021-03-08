package com.asangarin.breaker.network;

import lombok.Getter;

@Getter
public class BlockDigPacketInfo {
	private final int x, y, z;
	private final DigType digType;

	public BlockDigPacketInfo(int x, int y, int z, String name) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.digType = fromName(name);
	}

	public enum DigType {
		START,
		STOP,
		ABORT,
		INVALID;
	}

	private DigType fromName(String name) {
		switch(name.toUpperCase()) {
			case "START_DESTROY_BLOCK":
				return DigType.START;
			case "STOP_DESTROY_BLOCK":
				return DigType.STOP;
			case "ABORT_DESTROY_BLOCK":
				return DigType.ABORT;
			default:
				return DigType.INVALID;
		}
	}
}
