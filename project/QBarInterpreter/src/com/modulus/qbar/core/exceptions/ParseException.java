package com.modulus.qbar.core.exceptions;

import com.modulus.dataread.expressions.Statement;

public class ParseException extends RuntimeException{
	private static final long serialVersionUID = 5108754994935394009L;
	private int line;
	public ParseException(Statement stmt) {
		super();
		line = stmt.getLineNumber();
	}

	public ParseException(String message, Throwable cause, Statement stmt) {
		super(message + " Line: " + stmt.getLineNumber() + "\n" + "Near: " + stmt.getHeader(), cause);
		line = stmt.getLineNumber();
	}

	public ParseException(String message, Statement stmt) {
		super(message + " Line: " + stmt.getLineNumber()+ "\n" + "Near: " + stmt.getHeader());
		line = stmt.getLineNumber();
	}

	public ParseException(Throwable cause, Statement stmt) {
		super(cause);
		line = stmt.getLineNumber();
	}
	
	public int getLineNumber(){
		return line;
	}
	
}
