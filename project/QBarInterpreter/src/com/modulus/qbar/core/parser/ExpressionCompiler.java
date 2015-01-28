package com.modulus.qbar.core.parser;

import java.util.ArrayList;
import java.util.List;

import com.modulus.dataread.expressions.ExpressionPart;
import com.modulus.qbar.core.compile.ByteOps;
import com.modulus.qbar.core.primitive.QBDouble;
import com.modulus.qbar.core.primitive.QBInt;

public class ExpressionCompiler {
	private QBExpressionParser parser = new QBExpressionParser();
	public ExpressionCompiler(){
		
	}
	
	public ByteOperation[] compile( String expression ){	
//		System.out.println("EXP: "+expression);
		List<ByteOperation> ret = new ArrayList<ByteOperation>();
		
		ExpressionPart part = parser.parseExpressions(expression);
		String compiled = part.compilePart();
		
		String[] split = compiled.split("\\s+");
		
		for ( String str : split ){
			if(str.length() > 0){
				if (str.startsWith("<-",1) ){
					ByteOperation tmp = ret.remove(ret.size()-1);
					ret.add( new ByteOperation(ByteOps.STO, tmp.getArgs() ) );
				} else if (str.startsWith("->",1) ){
					ByteOperation tmp = ret.remove(ret.size()-1);
					ret.add( new ByteOperation(ByteOps.ITR, tmp.getArgs() ) );
				} else if(str.charAt(0) == '\u00FF'){
					ret.add( new ByteOperation(ByteOps.CALL, str.substring(1)) );
				} else if(str.charAt(0) == '\u00FE'){
					ret.add( new ByteOperation(ByteOps.CALLG, str.substring(1)) );
				} else {
					
					if( Character.isDigit( str.charAt(0) ) || (str.charAt(0) == '-' && str.length() > 1 && Character.isDigit(str.charAt(1)) )) {
						if(str.contains(".") || str.endsWith("d")){
							if(str.endsWith("d"))
								str = str.substring(0, str.length() - 1);
							double dval = Double.parseDouble(str);
							ret.add(new ByteOperation( ByteOps.PUSHO, new QBDouble(dval) ));
						} else{
							if(str.endsWith("i"))
								str = str.substring(0, str.length() - 1);
							int dval = Integer.parseInt(str);
							ret.add(new ByteOperation( ByteOps.PUSHO, new QBInt(dval) ));
						}
						
					} else {
						ret.add( new ByteOperation( ByteOps.PUSH, str.split("\\.") ) );
					}
				}
			}
		}
		
		return ret.toArray( new ByteOperation[ret.size()] );
	}
}
