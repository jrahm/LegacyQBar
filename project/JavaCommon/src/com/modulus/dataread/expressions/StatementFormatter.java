package com.modulus.dataread.expressions;

import java.io.Serializable;

/**
 * Classes that implement this interface are used
 * to format string before they become a part of
 * a statement.
 * 
 * These classes are the classes responsible for making
 * sure the headers are formatted in such a way that they might
 * be easy to parse.
 * 
 * @author jrahm
 */
public interface StatementFormatter extends Serializable{
	/**
	 * Given the String <code>header</code> as a header of a statement,
	 * format it so it can easily be parsed by custom parsing
	 * techniques.
	 * 
	 * @param header the header to parse
	 * @return a formatted version of the header.
	 */
	String formatHeader(String header);
}
