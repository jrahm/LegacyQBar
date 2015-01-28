package com.modulus.qbar.integration;

import com.modulus.qbar.core.QBUtilityNamespace;

public class QBWrappedUtilityNamespace extends QBUtilityNamespace{
	private static final long serialVersionUID = -2579384501097845702L;
	private String name;
	
	public QBWrappedUtilityNamespace(String name){
		this.name = name;
	}
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void construct() {
	}
}
