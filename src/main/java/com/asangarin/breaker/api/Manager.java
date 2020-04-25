package com.asangarin.breaker.api;

import java.util.ArrayList;
import java.util.List;

public abstract class Manager<T> {
	List<Class<? extends T>> registered = new ArrayList<>();
	
	public Manager() { load(); }
	public abstract void load();
	
	public void register(Class<? extends T> stateClass)
	{ registered.add(stateClass); }
	public List<Class<? extends T>> registered()
	{ return registered; }
}
