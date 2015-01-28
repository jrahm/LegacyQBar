package com.modulus.access;

/**
 * Any kind of function that takes an array of type <code>E</code>
 * and returns an object of type <code>T</code>.
 * 
 * Usually, the return type and the argument types are the same type.
 * 
 * @author jrahm
 *
 * @param <T> the return type of this function
 * @param <E> the argument type of the function
 */
public interface ArrayFunction< T, E > {
	
	/**
	 * Executes this function using the arguments <code>args</code>
	 * and returns an object of type <code>T</code>
	 * 
	 * @param args the arguments to execute this function with.
	 * @return some value of type <code>T</code>
	 */
	public T execute( E[] args );
}
