package com.modulus.qbar.lang;

import java.util.Iterator;

import com.modulus.qbar.core.QBFunction;
import com.modulus.qbar.core.QBNamespace;
import com.modulus.qbar.core.QBObject;
import com.modulus.qbar.core.QBStruct;
import com.modulus.qbar.core.primitive.QBInt;

public abstract class QBIterator<T extends QBObject> extends QBObject implements Iterator<QBObject>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8496833881500665535L;
	private String variableName;
	private T iterable;
	
	public static final QBStruct ITERATOR = new QBStruct("Iterator");
	
	public static final QBFunction hasNext = new QBFunction(1){

		/**
		 * 
		 */
		private static final long serialVersionUID = -7059204966992996830L;

		@Override
		public QBObject execute(QBObject[] args) {
			QBObject obj = args[0];
			QBIterator iter = (QBIterator) obj;
			
			return iter.hasNext() ? new QBInt(1) : new QBInt(0);
		}
		
	};
	
	public static final QBFunction next = new QBFunction(1){

		/**
		 * 
		 */
		private static final long serialVersionUID = 9165087060843674645L;

		@Override
		public QBObject execute(QBObject[] args) {
			QBObject obj = args[0];
			
			@SuppressWarnings("unchecked")
			QBIterator<QBObject> iter = (QBIterator<QBObject>) obj;
			
			return iter.next();
		}
		
	};
	
	static{
		ITERATOR.set("hasNext", hasNext);
		ITERATOR.set("next", next);
	}
	
	private QBNamespace variableNmspce;
	
	public QBIterator(T iterable) {
		super(ITERATOR);
		this.iterable = iterable;
	}

	@Override
	public QBObject get(String str){
		QBNamespace sup = this.getSuper();
		
		if(sup == null)
			return null;
		
		return sup.get(str);
	}
	
	@Override
	public void set(String str, QBObject obj){
		QBNamespace sup = this.getSuper();
		
		if( this.variableName.equals(str) && sup != null)
			variableNmspce.set(str, obj);
		
		else
			super.set(str, obj);
	}

	@Override
	public void remove() {
		throw new RuntimeException("Not Implemented");
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public String getVariableName() {
		return variableName;
	}

	public T getIterable() {
		return iterable;
	}
	
	protected abstract QBObject nextObject();
	
	public QBObject next(){
		
		QBObject ret = nextObject();
		this.set(this.getVariableName(), ret);
		
		return ret;
	}

	public void setVariableNmspce(QBNamespace variableNmspce) {
		this.variableNmspce = variableNmspce;
	}

	public QBNamespace getVariableNmspce() {
		return variableNmspce;
	}
}
