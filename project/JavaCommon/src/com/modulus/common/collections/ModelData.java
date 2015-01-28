package com.modulus.common.collections;

public interface ModelData<T> {
	public T get(Object key);
	
	public void set(Object key, T obj);
	
	public Object[] keys();
}
