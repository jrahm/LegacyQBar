package com.modulus.qbar.lang;

import com.modulus.common.collections.Stack;
import com.modulus.qbar.core.QBObject;
import com.modulus.qbar.core.interpreter.QBInterpreter;
import com.modulus.qbar.core.parser.ByteOperation;

public class QBArrayListConstructor extends ListConstructor{
	private ByteOperation[][] expressions;

	
	public QBArrayListConstructor(ByteOperation[][] exp){
		this.expressions = exp;
	}
	@Override
	protected QBList makeList() {
		QBObject[] objs = new QBObject[expressions.length];
		Stack<QBObject> stack = QBInterpreter.instance().getStack();
		
		for(int i = 0;i < expressions.length;i++){
			this.exec( expressions[i], stack);
			objs[i] = returnedValue;
		}
		
		QBList arr = new QBArrayList(objs);
		return arr;
	}

}
