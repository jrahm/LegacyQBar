package com.modulus.qbar.lang;

import com.modulus.qbar.core.QBFunction;

public class QBFunctionListConstructor extends ListConstructor{
	private static final long serialVersionUID = 5847772733369552908L;
	private QBFunction func;

	public QBFunctionListConstructor(QBFunction func){
		this.func = func;
	}
	
	@Override
	protected QBList makeList() {
		QBList lst =  new QBFunctionList(func);
		return lst;
	}
}
