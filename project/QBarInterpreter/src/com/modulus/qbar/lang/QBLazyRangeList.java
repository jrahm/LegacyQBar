package com.modulus.qbar.lang;

import java.util.ArrayList;
import java.util.List;

import com.modulus.qbar.core.QBObject;
import com.modulus.qbar.core.primitive.QBDouble;
import com.modulus.qbar.core.primitive.QBInt;
import com.modulus.qbar.core.primitive.QBPrimitive;

public class QBLazyRangeList extends QBList{
	private static final long serialVersionUID = -782755006057179634L;
	private QBPrimitive startValue;
	private QBPrimitive endValue;
	private QBPrimitive changeValue;
	
	private boolean isIntegers;
	private boolean isInfinite;
	
	//private List<QBPrimitive> items = new ArrayList<QBPrimitive>();

	public QBLazyRangeList(QBPrimitive startValue, QBPrimitive endValue,
			QBPrimitive changeValue) {
		super();
		this.startValue = startValue;
		this.endValue = endValue;
		this.changeValue = changeValue;
		
		if(endValue == null)
			isInfinite = true;
		
		if(changeValue == null)
			this.changeValue = new QBInt(1);
		
		if(this.changeValue instanceof QBInt && this.startValue instanceof QBInt)
			isIntegers = true;
		
	}

	@Override
	public QBObject add(QBObject obj) {
		return this;
	}

	@Override
	public void remove(QBObject obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public QBPrimitive get(int idx) {
		if(idx < 0)
			idx = (int) (this.size() + idx);
		
		if( isIntegers )
		{
			int ret = startValue.intValue() + idx * changeValue.intValue();
			if( this.isInfinite || ret <= endValue.intValue() )
				return new QBInt(ret);
			else
				throw new ArrayIndexOutOfBoundsException("The index: " + idx + " is out of range for " + this + "\nMax value is: " + endValue + " got " + ret);
		} else{
			double ret = startValue.doubleValue() + idx * changeValue.doubleValue();
			if( this.isInfinite || ret <= endValue.doubleValue() )
				return new QBDouble(ret);
			else
				throw new ArrayIndexOutOfBoundsException("The index: " + idx + " is out of range for " + this + "\nMax value is: " + endValue + " got " + ret);		
		}
	}

	@Override
	public double size() {
		if(isInfinite)
			return Double.POSITIVE_INFINITY;
		
		else
			return Math.abs(endValue.doubleValue() - startValue.doubleValue()) / changeValue.doubleValue() + 1;
	}

	@Override
	public void insert(int idx, QBObject obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public QBList realSubList(int off, int len) {
		return new QBLazyRangeList(get(off), get(off + len), changeValue);
	}

}
