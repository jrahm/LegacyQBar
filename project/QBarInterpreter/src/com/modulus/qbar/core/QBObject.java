package com.modulus.qbar.core;

import java.util.Hashtable;

import com.modulus.qbar.integration.QBWrappedObject;
import com.modulus.qbar.lang.QBIterator;
import com.modulus.qbar.lang.QBSyntheticIterator;

public class QBObject extends QBNamespace implements Cloneable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3738912814141895851L;
	private QBStruct struct;

	
	public QBObject( QBStruct struct ){
		super( new Hashtable<String, QBObject>(), struct );
		this.struct = struct;
	}
	
	public QBStruct getStruct(){
		return struct;
	}
	
	protected void setStruct(QBStruct struct){
		this.struct = struct;
	}
	
	public Object getWrapped(){
		// throw new RuntimeException("The type: " + struct.getName() + " is not of native origin.");
		return this;
	}
//	@Override
//	public void setSuper(QBNamespace sup){
//		new Exception().printStackTrace();
//		super.setSuper(sup);
//	}
	
	public QBObject clone(){
		try {
			return (QBObject) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public QBIterator<?> iterator(String varName, QBNamespace sup){
		QBFunction iterator = (QBFunction) this.get("iterator");

		if(iterator == null)
			return null;
		
		QBIterator<?> ret;
		
		QBObject obj = iterator.execute(new QBObject[]{this});
		
		if(!(obj instanceof QBIterator))
			ret = new QBSyntheticIterator(this, obj);
		else
			ret = (QBIterator<?>) obj;
		
		ret.setVariableName(varName);
		ret.setVariableNmspce(sup);
		
		return ret;
	}
	
	public static long totalTime = 0;
	public QBObject get(String name){
		long t1 = System.currentTimeMillis();
		try{
			QBObject ret = super.get(name);
			long t2 = System.currentTimeMillis();
			totalTime += (t2 -t1);
			return ret;
		} catch(Exception e){
			throw new RuntimeException("The variable: " + name + " does not exist for object: " + this);
		}

	}
	
	public String toString(){
		String tmp = super.toString();
	
		return (struct == null ? "QBObject" : struct.getName()) + tmp.substring(tmp.indexOf("@"));
	}
}
