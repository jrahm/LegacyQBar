package com.modulus.qbar.core.parser;

import com.modulus.qbar.core.QBFunction;
import com.modulus.qbar.lang.IfThenElseFunction;

public class IfThenElseFunctionFactory {
	private static ExpressionCompiler compiler = new ExpressionCompiler();
	
	public static QBFunction makeIfThenElse(String ifPart, String thenPart, String elsePart){
		ByteOperation[] ifArr = compiler.compile(ifPart);
		ByteOperation[] thenArr = compiler.compile(thenPart);
		ByteOperation[] elseArr = compiler.compile(elsePart);
		
		return new IfThenElseFunction(ifArr, thenArr, elseArr);
	}
}
