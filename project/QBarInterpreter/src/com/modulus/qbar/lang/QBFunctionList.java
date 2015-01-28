package com.modulus.qbar.lang;

import java.util.HashMap;
import java.util.Map;

import com.modulus.qbar.core.QBFunction;
import com.modulus.qbar.core.QBObject;
import com.modulus.qbar.core.primitive.QBInt;

public class QBFunctionList extends QBList{
	private static final long serialVersionUID = -6236925720933913879L;
	private Map<Integer, QBObject> listPart = new HashMap<Integer, QBObject>();
	private QBFunction func;
	
	public QBFunctionList(QBFunction func){
		this.func = func;
	}
	
	@Override
	public QBObject add(QBObject obj) {
		return this;
	}

	@Override
	public void remove(QBObject obj) {
		
	}

	@Override
	public QBObject get(int idx) {
		QBObject tmp = listPart.get(idx);
		
		if(tmp == null)
			listPart.put(idx, tmp = func.execute(new QBObject[]{new QBInt(idx), this}));
		
		return tmp;
	}

	@Override
	public double size() {
		return Double.POSITIVE_INFINITY;
	}

	@Override
	public void insert(int idx, QBObject obj) {
		listPart.put(idx, obj);
	}

	@Override
	public QBList realSubList(int off, int len) {
		throw new RuntimeException("Sub-list type not supported for FunctionList.");
	}
	
}
