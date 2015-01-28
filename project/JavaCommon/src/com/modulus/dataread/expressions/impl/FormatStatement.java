package com.modulus.dataread.expressions.impl;

import com.modulus.dataread.expressions.AbstractStatement;
import com.modulus.dataread.expressions.StatementFormatter;

/**
 * This class that extends AbstractStatement will first
 * format its header before it sets the actual header to
 * what the user sends, using a statement formatter.
 * 
 * @author jrahm
 *
 */
public class FormatStatement extends AbstractStatement{
	private static final long serialVersionUID = -4973798175965745298L;
	private StatementFormatter formatter;
	
	/**
	 * This creates a new Statement with <code>formatter</code>
	 * as the default formatter for this Statement.
	 * 
	 * @param formatter the formatter for this statement
	 */
	public FormatStatement(StatementFormatter formatter){
		this.formatter = formatter;
	}
	
	protected FormatStatement(){
	}
	
	@Override
	public void setHeader(String header) {
		this.header = formatter.formatHeader(header);
	}
	
	/**
	 * Returns the formatter for this Statement
	 * @return the formatter for this Statement
	 */
	public StatementFormatter getFormatter(){
		return formatter;
	}
	
	/**
	 * Sets the formatter for this statement
	 * @param formatter the formatter for this statement
	 */
	public void setFormatter(StatementFormatter formatter){
		this.formatter = formatter;
	}
	
}
