package com.modulus.qbar.core.interpreter;
import com.modulus.qbar.core.QBObject;
import com.modulus.qbar.core.QBSyntheticFunction;
import com.modulus.qbar.core.parser.QBParser;
import com.modulus.qbar.core.parser.stmt.QBStatement;

public class TransparentFunction extends QBSyntheticFunction {
	private QBInteractiveInterpreter ths;
	public TransparentFunction(QBStatement code, QBParser parser, QBInteractiveInterpreter ths) {
		super(code, parser);
		this.ths = ths;
	}
	
	@Override
	public QBObject get( String name ){
		return ths.getGlobalNamespace().get( name );
	}
	
	@Override
	public void set( String name, QBObject obj ) {
		ths.getGlobalNamespace().set(name, obj);
	}
	
}