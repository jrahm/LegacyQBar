package com.modulus.common.collections;

/**
 * Class defines a better used stack than what is
 * in the java api. This stack is an interface
 * with many different implementations.
 * 
 * @author jrahm
 *
 * @param <T> the type this stack stores
 */
public interface Stack<T> {
	/**
	 * Removes and returns the element that is on
	 * the top of the stack.
	 * 
	 * @return the element which is on the top of the stack.
	 */
	public T pop();
	
	/**
	 * Places object <code>obj</code> on the top of
	 * the stack
	 * 
	 * @param obj the object to push on the stack.
	 */
	public void push(T obj);
	
	/**
	 * Returns the object on the top of the stack
	 * but does <b>not</b> remove it.
	 * 
	 * @return the object on the top of the stack
	 */
	public T peek();
	
	/**
	 * Returns the number of elements in this
	 * stack.
	 * 
	 * @return the number of elements in this stack.
	 */
	public int size();
	
	/**
	 * Returns true of this stack has no elements,
	 * returns false otherwise.
	 * 
	 * @return true it this stack has no elements, false otherwise
	 */
	public boolean isEmpty();
	
	/**
	 * Empties the stack.
	 */
	public void clear();
}
