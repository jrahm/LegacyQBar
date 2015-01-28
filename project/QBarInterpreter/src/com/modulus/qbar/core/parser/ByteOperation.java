package com.modulus.qbar.core.parser;

import java.io.Serializable;
import java.util.Arrays;

import com.modulus.qbar.core.compile.ByteOps;

public class ByteOperation implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8120121479968460423L;
	private ByteOps operator;
	private Object[] args = {null};
	
	public ByteOperation(ByteOps op, Object arg){
		this.operator = op;
		this.args = new Object[]{arg};
	}
	
	public ByteOperation(ByteOps op, Object[] args){
		this.operator = op;
		
		if(args.length > 0)
			this.args = args;
	}
	
	public ByteOps getOperator(){
		return operator;
	}
	
	public Object[] getArgs(){
		return args;
	}
	
	public Object getArg(){
		return args[0];
	}
	
	public String toString(){
		return operator.toString() + " " + Arrays.toString(args);
	}
}
