package com.modulus.common;

/**
 * Class that simply holds two different (or same) object
 * types. It is a rough implementation of what a tuple can be
 * considered
 * 
 * @author jrahm
 *
 * @param <T> type of object 1
 * @param <E> type of object 2
 */
public class TwoTypeBean<T, E> {
	private T obj1;
	private E obj2;
	
	/**
	 * Creates a new TwoTypeBean from the two
	 * objects.
	 * 
	 * @param obj1 object 1
	 * @param obj2 object 2
	 */
	public TwoTypeBean(T obj1, E obj2){
		this.obj1 = obj1;
		this.obj2 = obj2;
	}
	
	/**
	 * Returns the first object
	 * @return the first object
	 */
	public T getObject1(){
		return obj1;
	}
	
	/**
	 * Returns the second object
	 * @return the second object
	 */
	public E getObject2(){
		return obj2;
	}
}
