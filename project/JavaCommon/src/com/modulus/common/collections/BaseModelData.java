package com.modulus.common.collections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BaseModelData<T> implements ModelData<T>{
	private Map<Object, T> map = new HashMap<Object, T>();
	
	@Override
	public T get(Object key) {
		return map.get(key);
	}

	@Override
	public Object[] keys() {
		Set<Object> keys = map.keySet();
		
		return keys.toArray( new Object[keys.size()] );
	}

	@Override
	public void set(Object key, Object obj) {
		// TODO Auto-generated method stub
		
	}
	
	
}
