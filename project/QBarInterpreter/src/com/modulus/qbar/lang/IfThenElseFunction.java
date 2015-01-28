package com.modulus.qbar.lang;

import com.modulus.common.collections.Stack;
import com.modulus.qbar.core.QBFunction;
import com.modulus.qbar.core.QBObject;
import com.modulus.qbar.core.interpreter.QBInterpreter;
import com.modulus.qbar.core.parser.ByteOperation;
import com.modulus.qbar.core.primitive.QBPrimitive;

public class IfThenElseFunction extends QBFunction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6694512700264765780L;
	private ByteOperation[] ifPart, thenPart, elsePart;
	

	public IfThenElseFunction(ByteOperation[] ifPart,
			ByteOperation[] thenPart, ByteOperation[] elsePart) {
		super(0);
		this.ifPart = ifPart;
		this.thenPart = thenPart;
		this.elsePart = elsePart;
	}


	@Override
	public QBObject execute(QBObject[] args) {
		Stack<QBObject> stack = QBInterpreter.instance().getStack();
		this.exec(ifPart, stack);
		
		if(returnedValue instanceof QBPrimitive && ((QBPrimitive) returnedValue).intValue() != 0){
			this.exec(thenPart, stack);
			return returnedValue;
		} else{
			this.exec(elsePart, stack);
			return returnedValue;
		}
	}

}
