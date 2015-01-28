package com.modulus.dataread.expressions;

/**
 * Interface which describes how an expression parser
 * will handle information from an interpreter file.
 * 
 * @author jrahm
 *
 * @param S the statement type which is used to create this class
 */
public interface StatementTreeParser {
	
	/**
	 * Parse a block of code into statements using the statement factory <code>factory</code>
	 * to generate the statements to use.
	 * @param <T> the type of statement to use.
	 * @param factory the factory to generate the statements
	 * @return a Statement of type <code>T</code> that represents the code parsed
	 */
	<T extends Statement> Statement parseStatements(StatementFactory<T> factory);
}
