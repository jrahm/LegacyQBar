package com.modulus.common.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IntMap<T> implements Map<Integer, T> {
	private List<T> list = new ArrayList<T>();
	
	@Override
	public void clear() {
		this.list.clear();
	}

	@Override
	public boolean containsKey(Object arg0) {
		if( arg0 instanceof Integer)
			return ((Integer) arg0) < this.list.size();
		
		return false;
	}

	@Override
	public boolean containsValue(Object arg0) {
		return list.contains(arg0);
	}

	@Override
	public Set<java.util.Map.Entry<Integer, T>> entrySet() {
		throw new RuntimeException("Not Implemented (At The Moment)");
	}

	@Override
	public T get(Object arg0) {
		try{
			return list.get( (Integer)arg0 );
		} catch(ClassCastException e){
			return null;
		}
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public Set<Integer> keySet() {
		return new HashSet<Integer>( Arrays.asList( Collections2.rangeAsObjects(0, list.size()) ) );
	}

	@Override
	public T put(Integer arg0, T arg1) {
		return list.set(arg0, arg1);
	}

	@Override
	public void putAll(Map<? extends Integer, ? extends T> arg0) {
		throw new RuntimeException("Not Implemented At the Moment");
	}

	@Override
	public T remove(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Collection<T> values() {
		// TODO Auto-generated method stub
		return null;
	}

}
