package com.modulus.dataread.expressions.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import com.modulus.common.TwoTypeBean;
import com.modulus.dataread.expressions.LineNumberException;
import com.modulus.dataread.expressions.StatementFormatter;
import com.modulus.dataread.expressions.StatementTreeParser;
import com.modulus.dataread.expressions.ParseRules;
import com.modulus.dataread.expressions.Statement;
import com.modulus.dataread.expressions.StatementFactory;

/**
 * This simple expression parser is meant not to do anything fancy
 * all it does is break up the file into a tree based on the folders
 * in the file.
 * 
 * @author jrahm
 *
 */
public class SimpleStatementTreeParser implements StatementTreeParser{

	/*
	 * used by methods to keep track of
	 * the number of lines
	 */
	private int line = 0;
	/*
	 * The rules to parse by
	 */
	private ParseRules rules;
	
	/**
	 * Creates a new <code>SimpleExpressionParser</code> based
	 * off of the rules that was given to it.
	 * 
	 * @param rules the rules to parse by
	 */
	public SimpleStatementTreeParser(ParseRules rules){
		this.rules = rules;
	}

	@Override
	public <T extends Statement> Statement parseStatements(StatementFactory<T> factory) {
		// set the line counter to 1
		this.line = 0;
		return recurParseStatement(factory, 0).getObject1();
	}
	
	/*
	 * Setup a recursive parser to 
	 * return a tree of statements
	 */
	private <T extends Statement> TwoTypeBean<T, Integer>  recurParseStatement(StatementFactory<T> factory, int off){
		try{
			// System.out.println("  parseStatements");
			final String code = rules.getCode();
			
			// statement which will be returned in the end,
			// this statement is the parent of all the other
			// statements
			T ret = factory.generateStatement(null);
			int i = off;
			int n = 0;
			
			// start an iteration through the
			// string
			for( ;i < code.length(); n++, i += rules.read(i) ){
				// if the next character is a new line character,
				// the increment the line counter
				// System.out.println( i );
				if(code.charAt(i) == '\n'){
					line ++;
					// that is all a new line does;
					continue;
				}
				
				// check to see if the rules say that here a statement
				// is to be terminated
				if( rules.statementTerminated(i) ){
					
					// if so, then it is time to manifest a new statement
					// header is equal to the substring of the code from
					// off to i
					String header = code.substring(off, i);
					
					// create a new child from the type provided
					T child = factory.generateStatement(null);
					
					// set the header
					child.setHeader( header );
					
					// set the line number to what it should be
					child.setLineNumber( this.line );
					
					// we are done with creating this beam, so it is time to
					// return it
					return new TwoTypeBean<T, Integer>(child, i); 
				}
				
				// if the rules say that at this point,
				// a folder is opened, then it is time to
				// use recursion to implement a tree.
				else if ( rules.openFolder(i) ){
					
					// the header is from off to when the
					// folder is being closed
					String header = code.substring(off, i);
					
					// set the header
					ret.setHeader(header);
					
					// set the line number
					ret.setLineNumber(line);
					
					// it is time to increment i by the
					// amount the rules say to.
					i += rules.read(i);

					// while the folder is not closed
					while( !rules.closeFolder(i) ){
						
						// read the next statement
						TwoTypeBean<T, Integer> bean = recurParseStatement(factory, i);
						
						// the second object is the offset
						//that the previous call terminated with
						i = bean.getObject2();
						
						// add this child to the main bean
						ret.addChild(bean.getObject1());
						
						// increment the incrementer by how
						// much the rules say to
						i += rules.read(i);
						
						while(code.charAt(i) == '\n'){
							line ++;
							i++;
						}
					}
					
					// When finished reading this file, it is time
					// to return the main bean.
					return new TwoTypeBean<T, Integer>(ret, i);
				}

			}
			
			// System.out.println(" /parseStatements");
			return new TwoTypeBean<T, Integer>(ret, i);
		} catch( Exception e ){
			throw new LineNumberException("Sytax error", e, line);
		}
	}

}
