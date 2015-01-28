package com.modulus.qbar.core;

public abstract class QBUtilityNamespace extends QBObject{
	private static final long serialVersionUID = -5228021096385368069L;

	public QBUtilityNamespace() {
		super( new QBStruct("Namespace") );
	}
	
	public QBUtilityNamespace( QBStruct struct ){
		super(struct);
	}
	
	public abstract String getName();
	
	public abstract void construct();
	
	public void setStruct( QBStruct struct ){
		super.setStruct(struct);
	}
	
}
