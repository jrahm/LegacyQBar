package com.modulus.qbar.core;

import java.util.Arrays;
import java.util.Collections;

import com.modulus.dataread.expressions.Statement;
import com.modulus.qbar.core.exceptions.ParseException;
import com.modulus.qbar.core.interpreter.QBInterpreter;
import com.modulus.qbar.core.parser.QBParser;
import com.modulus.qbar.lang.QBArrayList;
import com.modulus.qbar.lang.QBList;

public class QBSyntheticStruct extends QBStruct{
	private static final long serialVersionUID = -1080935988322343396L;
	// This maps an instance variable to its default value, if the struct is null
	// the instance will be null defaultly
	// private Map<String, QBStruct> instanceVars = new HashMap<String, QBStruct>();
	private Statement code;
	
	public QBSyntheticStruct( Statement code, QBParser parser, QBNamespace sup ) {
		super(false);
		
		this.setSuper(sup);
		this.code = code;
		if( !code.getHeader().startsWith("Struct") ){
			throw new ParseException("Syntax error while creating struct, expected `Struct`", code);
		}
		// code header should be in the form of `Struct someStruct = `
		
		String[] split = code.getHeader().split("\\s+");
		
		if( code.getHeader().contains("extends") ){
			QBList supers = new QBArrayList();
			this.set( "supers", supers );
			
			String header = code.getHeader();
			String ext = header.split("\\s+extends\\s+")[1];
			
			String[] extending = ext.split("\\s*,\\s*");
			Collections.reverse(Arrays.asList(extending));
			
			for(String s : extending){
				QBStruct obj = (QBStruct) get(s);
				this.addSuperStruct(obj);
				
				supers.add(obj);
			}
		}
		
		String name = split[1];
		
		this.setName(name);
		
		for(Statement child : code.getChildren())
			parser.readStatement(child, this);
	}

	@Override
	public QBObject newInstance() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toString(){
		return code.toString();
	}

}
