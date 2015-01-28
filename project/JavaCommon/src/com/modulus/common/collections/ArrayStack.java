package com.modulus.common.collections;

import java.io.Serializable;

/**
 * Class which uses an array to be used to
 * implement a stack.
 * 
 * This class is most efficient for
 * nearly every purpose.
 * 
 * @author jrahm
 *
 * @param <T> the type this stack holds
 */
public class ArrayStack<T> implements Stack<T>, Serializable{
	
	private Object[] arr;
	private int pointer = -1;
	
	/**
	 * Creates a new ArrayStack with a
	 * default starting size of 10
	 */
	public ArrayStack(){
		this(10);
	}
	
	/**
	 * Creates a new ArrayStack with
	 * a default starting size of <code>size</code>
	 * @param size
	 */
	public ArrayStack( int size ){
		arr = new Object[size];
	}
	
	/**
	 * Ensures that this ArrayStack has enough space
	 * to store <code>size</code> number of elements.
	 * 
	 * @param size the number of elements needed to store
	 */
	public void ensureSize( int size ){

		if(arr.length > size)
			return;
		
		Object[] tmp = new Object[size*2];
		for ( int i = 0;i < arr.length; i ++ ){
			tmp[i] = arr[i];
		}
		
		arr = tmp;
	}

	@Override
	public boolean isEmpty() {
		return pointer == -1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T peek() {
		return (T)arr[pointer];
	}

	@SuppressWarnings("unchecked")
	@Override
	public T pop() {
		return (T) arr[ pointer -- ];
	}

	@Override
	public void push(T obj) {
		pointer ++;
		this.ensureSize(pointer);
		arr[pointer] = obj;
	}

	@Override
	public int size() {
		return pointer + 1;
	}
	
	@Override
	public void clear(){
		pointer = 0;
	}
	
	@Override
	public String toString(){
		StringBuffer buf = new StringBuffer("[");
		
		for( int i = pointer; i >= 1; i --)
			buf.append(arr[i] + ", ");
		
		buf.append(arr[0] + "]");
		return buf.toString();
	}
}
