package com.modulus.dataread.expressions.impl;

import com.modulus.dataread.expressions.AbstractStatement;

public class SimpleStatement extends AbstractStatement {
	private static final long serialVersionUID = -8730515046885236907L;

	public SimpleStatement(){}
	
	public SimpleStatement(String string) {
		super();
		this.setHeader(string);
	}

	@Override
	public void setHeader(String header) {
		this.header = header;
	}
}
