package com.modulus.qbar.lang;

import com.modulus.qbar.core.QBFunction;
import com.modulus.qbar.core.QBObject;

public abstract class ListConstructor extends QBFunction{

	public ListConstructor() {
		super(0);
	}
	
	public QBList execute(QBObject[] args){
		return makeList();
	}
	
	protected abstract QBList makeList();
}
