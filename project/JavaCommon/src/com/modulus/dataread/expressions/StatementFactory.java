package com.modulus.dataread.expressions;

import java.util.Map;

/**
 * Interface describes how an object is supposed to
 * generate statements. This class is to be used int tandem
 * with the ExpressionParser interface to generate
 * Statements of a specific type;
 * 
 * @author jrahm
 *
 * @param <T> the type of statement this StatementFactory creates
 */
public interface StatementFactory<T extends Statement> {
	
	/**
	 * Creates a new statement with the type <code>T</code> based
	 * on what the parameters contain.
	 * 
	 * The parameters contain useful information about where the parser
	 * currently is and how to create the new statement.
	 * 
	 * @param params the parameters
	 * @return a new statement of type <code>T</code> based off of the parameters.
	 */
	T generateStatement(Map<String, Object> params);
}
