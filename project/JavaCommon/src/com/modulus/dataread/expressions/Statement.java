package com.modulus.dataread.expressions;

/**
 * This interface is used to store interpreted
 * information from a text file in a way that
 * most represents a tree. for easy parsing
 * for an interpreter or compiler.
 * 
 * @author jrahm
 *
 */
public interface Statement {
	
	/**
	 * Returns the child statements of this class
	 * 
	 * @return the child statements of this class
	 */
	Statement[] getChildren();
	
	/**
	 * Adds a child statement to this Statement
	 * 
	 * @param child the child statement to add
	 */
	void addChild(Statement child);
	
	/**
	 * Removes a child statement from this Statement object
	 * 
	 * @param child this child to remove from this statement
	 */
	void removeChild(Statement child);

	/**
	 * Sets the header for this statement object
	 * 
	 * @param header the header for this statement.
	 */
	void setHeader(String header);

	/**
	 * returns the header for this statement object
	 * @return
	 */
	String getHeader();
	
	/**
	 * side effect method to get nicely formatted printouts
	 * of the statement class.
	 * @param recur
	 * @return
	 */
	String toString( int recur );
	
	/**
	 * Returns the child of this Statement that has
	 * the header <code>header</code>. If there is no
	 * child with that header, then the method should
	 * then return <code>null</code>. If there are multiple
	 * children with the same header, then the first one should
	 * be returned. If all children with that header should be
	 * returned, then use the <code>getChildrenByHeader</code> method.
	 * 
	 * @param header the header of the child to return
	 * @return a child with the header <code>header</code>
	 */
	Statement getChildByHeader(String header);
	
	/**
	 * Returns an array of children of this Statement that have
	 * the header <code>header</code>. If no children have that header,
	 * then an empty array is returned.
	 * 
	 * @param header the header of the children to return.
	 * @return an array of children that have the header <code>header</code>
	 */
	Statement[] getChildrenByHeader(String header);
	
	/**
	 * Returns the line number that this statement starts on.
	 * @return the line number that this statement starts on.
	 */
	int getLineNumber();
	
	/**
	 * Sets the line number that this statement
	 * starts on.
	 * 
	 * @param line the line this statement starts on.
	 */
	void setLineNumber(int line);
	
	/**
	 * Returns true if this statement has children, false otherwise.
	 * 
	 * @return true if this statement has children.
	 */
	boolean hasChildren();
	
	/**
	 * Deletes all children from this statement
	 */
	void clearChildren();
}