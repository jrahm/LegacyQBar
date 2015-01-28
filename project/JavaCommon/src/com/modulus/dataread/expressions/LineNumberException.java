package com.modulus.dataread.expressions;

public class LineNumberException extends RuntimeException{
	private int line;
	
	public LineNumberException( int line ) {
		super();
		this.line = line;
	}

	public LineNumberException(String message, int line) {
		super(message + " line " + line);
		this.line = line;
	}

	public LineNumberException(String message, Throwable cause, int line) {
		super(message + " line " + line, cause);
		this.line = line;
	}
	
	public int getLine(){
		return line;
	}
	
}
