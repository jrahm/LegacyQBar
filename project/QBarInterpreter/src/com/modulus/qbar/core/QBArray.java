package com.modulus.qbar.core;

public class QBArray extends QBObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5554983175051078215L;
	private QBObject[] arr;
	
	public QBArray(int len){
		super( QBStruct.array );
		this.arr = new QBObject[len];
	}
	
	public void set(int idx, QBObject obj){
		this.arr[idx] = obj;
	}
	
	public QBObject get(int idx){
		return this.arr[idx];
	}
	
	public int getLength(){
		return arr.length;
	}
}
