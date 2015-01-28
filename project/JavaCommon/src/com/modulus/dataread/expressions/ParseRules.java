package com.modulus.dataread.expressions;

/**
 * Interface defines a set of rules used to parse
 * text files. These rules include such rules like
 * in string or folder start and end etc.
 * 
 * @author jrahm
 *
 */
public interface ParseRules {
	/**
	 * First formats the code to make
	 * life much easier for the parser.
	 * 
	 * This should be the first method to be
	 * called at all times.
	 */
	void format();
	
	/**
	 * Signals these parse rules that the user has read the character at
	 * the point <code>off</code> of which the parser dictates how much
	 * code to skip.
	 * 
	 * @param off point in the code which was read
	 * @return how many characters the parser should skip.
	 */
	int read( int off );
	
	/**
	 * Returns true if the parser is in quotes, false otherwise.
	 * 
	 * @return true if the parser is in quotes, false otherwuse. 
	 */
	boolean inQuotes();
	
	/**
	 * Returns true if the parser is in a
	 * comment or not.
	 * 
	 * @return true if the parser is in a
	 * comment or not.
	 */
	boolean inComment();
	
	/**
	 * returns true if at position <code>off</code>
	 * a folder is being opened.
	 * 
	 * @param off the position in the code
	 * @return true if at position <code>off</code> a
	 * folder is being opened.
	 */
	boolean openFolder( int off );
	
	/**
	 * returns true if at position <code>off</code>
	 * a folder is being closed in the code.
	 * 
	 * @param off the position of the parser
	 * @return if a folder is being closed at position <code>off</code>
	 */
	boolean closeFolder( int off );
	
	/**
	 * returns true if a statement is terminated at position <code>off</code>
	 * 
	 * @param off the position in the code.
	 * @return true if a statement is terminated at position <code>off</code>
	 */
	boolean statementTerminated( int off );
	
	/**
	 * Returns the code which these parse rules are using.
	 * @return the code which these parse rules are using.
	 */
	String getCode();
	
	/**
	 * Returns the line number of this reader
	 */
	int getLineNumber();
}
