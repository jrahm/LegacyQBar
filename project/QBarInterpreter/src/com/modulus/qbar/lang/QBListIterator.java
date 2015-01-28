package com.modulus.qbar.lang;

import com.modulus.qbar.core.QBObject;

public class QBListIterator extends QBIterator<QBList> {
	private static final long serialVersionUID = 8306618418948706270L;
	int index = 0;;
	public QBListIterator(QBList lst){
		super(lst);
		
		if(lst == null)
			throw new RuntimeException("How the Hell is this NULL?");
	}
	
	@Override
	public boolean hasNext() {
		QBList iterable = this.getIterable();
		return index < iterable.size();
	}

	@Override
	protected QBObject nextObject() {
		QBList lst = this.getIterable();
		
		QBObject ret = lst.get(index ++);
		
		return ret;
	}

}
