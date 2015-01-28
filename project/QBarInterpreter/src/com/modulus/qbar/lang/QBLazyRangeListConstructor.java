package com.modulus.qbar.lang;

import com.modulus.common.collections.Stack;
import com.modulus.qbar.core.QBObject;
import com.modulus.qbar.core.interpreter.QBInterpreter;
import com.modulus.qbar.core.parser.ByteOperation;
import com.modulus.qbar.core.primitive.QBDouble;
import com.modulus.qbar.core.primitive.QBInt;
import com.modulus.qbar.core.primitive.QBPrimitive;

public class QBLazyRangeListConstructor extends ListConstructor{
	private static final long serialVersionUID = -3617305937103054888L;

	private ByteOperation[] start;
	private ByteOperation[] second;
	private ByteOperation[] end;
	
	public QBLazyRangeListConstructor(ByteOperation[] start,
			ByteOperation[] second, ByteOperation[] end) {
		super();
		this.start = start;
		this.second = second;
		this.end = end;
	}

	@Override
	protected QBList makeList() {
		Stack<QBObject> stack = QBInterpreter.instance().getStack();
		
		QBPrimitive startValue;
		if(start == null){
			startValue = null;
		} else{
			exec( start, stack );
			startValue = (QBPrimitive) returnedValue;
		}
		
		QBPrimitive secondValue;
		if(second == null){
			secondValue = null;
		} else {
			exec( second, stack );
			secondValue = (QBPrimitive) returnedValue;
		}
		
		QBPrimitive endValue;
		if(end == null){
			endValue = null;
		} else{
			exec( end, stack );
			endValue = (QBPrimitive) returnedValue;
		}
		
		double change = secondValue == null ? 1 : secondValue.doubleValue() - startValue.doubleValue();
		
		QBPrimitive changeValue;
		if(change % 1 == 0 && startValue instanceof QBInt)
			changeValue = new QBInt((int)change);
		else
			changeValue = new QBDouble(change);
		
		return new QBLazyRangeList(startValue, endValue, changeValue);
	}

}
