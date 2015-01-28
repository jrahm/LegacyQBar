package com.modulus.qbar.core;

import com.modulus.common.collections.ArrayStack;
import com.modulus.common.collections.Stack;
import com.modulus.qbar.core.parser.QBParser;
import com.modulus.qbar.core.parser.stmt.QBStatement;

public class QBConstructor extends QBSyntheticFunction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1795350337497901352L;
	private QBStruct struct;
	private Stack<QBObject> thses = new ArrayStack<QBObject>();
	
	public QBConstructor(QBStatement code, QBStruct struct, QBParser parser) {
		super(code, parser);
		
		this.struct = struct;
		this.setGlobal(true);
	}
	
	public QBStruct getObjectStruct(){
		return struct;
	}

	
	@Override
	public QBObject execute( QBObject[] arr ){
		QBObject ths = new QBObject(struct);
		this.thses.push(ths);
		super.execute(arr);
		this.thses.pop();
		return ths;
	}
	
	@Override
	public QBObject get(String str){

		if(str.equals("this"))
			return thses.peek();

		return super.get(str);
	}
}
