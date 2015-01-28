package com.modulus.qbar.core.exceptions;

public class ExecutionException extends RuntimeException{
	private static final long serialVersionUID = 5108754994935394009L;

	public ExecutionException() {
		super();
	}

	public ExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExecutionException(String message) {
		super(message);
	}

	public ExecutionException(Throwable cause) {
		super(cause);
	}

	
}
