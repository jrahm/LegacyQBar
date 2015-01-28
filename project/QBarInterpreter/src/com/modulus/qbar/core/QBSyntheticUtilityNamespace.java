package com.modulus.qbar.core;

import com.modulus.dataread.expressions.Statement;
import com.modulus.qbar.core.parser.QBParser;

public class QBSyntheticUtilityNamespace extends QBUtilityNamespace {
	private static final long serialVersionUID = 1941492008845020291L;
	
	private String name;
	
	public QBSyntheticUtilityNamespace(Statement stat, QBParser parser, QBNamespace superNamespace) {
		String header = stat.getHeader();
		this.setSuper(superNamespace);
		// chop off `Namespace`
		name = header.substring(9).trim();
		
		for(Statement child : stat.getChildren())
			parser.readStatement(child, this);
		
		this.set("this", this);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void construct() {
		QBObject obj = this.getNoError("construct");
		if( obj instanceof QBFunction ){
			((QBFunction) obj).execute( new QBObject[]{} );
		}
	}
	
	

}
