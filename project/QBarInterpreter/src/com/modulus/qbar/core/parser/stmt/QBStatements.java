package com.modulus.qbar.core.parser.stmt;

import com.modulus.dataread.expressions.Statement;

public class QBStatements {
	static public boolean stmtIsNamespace( Statement stmt ){
		return stmt.getHeader().startsWith("Namespace");
	}
	static public boolean stmtIsFunction( Statement stmt ){
		return stmt.getHeader().startsWith("let");
	}
	
	static public boolean stmtIsClass( Statement stmt ){
		return stmt.getHeader().startsWith("Struct");
	}

	static public boolean stmtIsConstructor( Statement stmt ){
		// constructors have the word `new` come after the `let` declaration
		// as in let new Object(a,b,c) = do . . .
		
		// A constructor does not nessecarily have to take the name of the object
		if(!stmtIsFunction(stmt)){
			return false;
		}
		String header = stmt.getHeader();
		header = header.substring(3).trim();
		return header.startsWith("new");
	}
	
	static public boolean stmtIsImport( Statement stmt ){
		return stmt.getHeader().startsWith("Import");
	}
	
	static public boolean stmtIsLoad( Statement stmt ){
		return stmt.getHeader().startsWith("Incorporate");
	}
}
