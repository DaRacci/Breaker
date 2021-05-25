package com.asangarin.breaker.system;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BlockHardness {
	private final int min, max, base;
}
