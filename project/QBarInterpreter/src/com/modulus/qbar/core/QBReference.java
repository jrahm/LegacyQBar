package com.modulus.qbar.core;

public class QBReference extends QBObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4662929761631550702L;
	private String str;
	
	public QBReference(String str) {
		super(null);
		this.str = str;
	}
	
	public String getString(){
		return str;
	}

}
