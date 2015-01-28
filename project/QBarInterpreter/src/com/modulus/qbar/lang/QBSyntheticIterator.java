package com.modulus.qbar.lang;

import com.modulus.qbar.core.QBFunction;
import com.modulus.qbar.core.QBObject;
import com.modulus.qbar.core.parser.Core;

public class QBSyntheticIterator extends QBIterator<QBObject>{
	private static final long serialVersionUID = -1890206471197459684L;
	private QBFunction next;
	private QBFunction hasNext;
	private QBObject wrapped;
	
	public QBSyntheticIterator(QBObject iterable, QBObject wrapped) {
		super(iterable);
		
		this.wrapped = wrapped;
		this.next = (QBFunction) wrapped.get("next");
		this.hasNext = (QBFunction) wrapped.get("hasNext");
	}

	@Override
	public boolean hasNext() {
		return Core.QBObjectIsTrue( hasNext.execute( new QBObject[]{wrapped} ) );
	}

	@Override
	protected QBObject nextObject() {
		return next.execute( new QBObject[]{wrapped} );
	}

}
