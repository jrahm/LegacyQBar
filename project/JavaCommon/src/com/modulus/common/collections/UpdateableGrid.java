package com.modulus.common.collections;

/**
 * Interface that extends grid to make a grid
 * mutable.
 * 
 * @author jrahm
 *
 * @param <T> the type this grid holds
 */
public interface UpdateableGrid<T> extends Grid<T>{
	/**
	 * sets the reference in row <code>row</code> and column <code>col</code>
	 * to <code>obj</code>
	 * 
	 * @param row the row
	 * @param col the column
	 * @param obj the object
	 */
	public void set(int row, int col, T obj);
}
