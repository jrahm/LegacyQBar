package com.modulus.common.collections;

/**
 * Interface describing an immutable grid interface.
 * 
 * @author jrahm
 *
 * @param <T> the type of object this grid holds
 */
public interface Grid<T> {
	/**
	 * Returns the object stored in
	 * row <code>row</code> and column
	 * <code>col</code>
	 * 
	 * @param row the row of the object
	 * @param col the column of the object
	 * @return the object stored in that row and column
	 */
	public T get(int row, int col);
	
	/**
	 * Returns the number of columns in this grid
	 * 
	 * @return the number of columns in this grid
	 */
	public int numberOfColumns();
	
	/**
	 * Returns the number of columns in this grid.
	 * 
	 * @return the number of columns in this grid
	 */
	public int numberOfRows();
}
