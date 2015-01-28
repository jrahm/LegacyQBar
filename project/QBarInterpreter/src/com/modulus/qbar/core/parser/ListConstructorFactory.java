package com.modulus.qbar.core.parser;

import java.util.ArrayList;
import java.util.Arrays;

import com.modulus.qbar.core.QBFunction;
import com.modulus.qbar.core.QBNamespace;
import com.modulus.qbar.core.exceptions.ExecutionException;
import com.modulus.qbar.lang.QBArrayListConstructor;
import com.modulus.qbar.lang.QBFunctionListConstructor;
import com.modulus.qbar.lang.QBFunctionListFunction;
import com.modulus.qbar.lang.QBLazyRangeListConstructor;

public abstract class ListConstructorFactory {
	private static final ExpressionCompiler compiler = new ExpressionCompiler();
	
	// DO NOT CUT OUT THE MIDDLE, THE PASSED STRING SHOUDL STILL HAVE THE BRACKETS
	public static QBFunction makeNewQBListConstructor(String code, QBNamespace sup, QBParser parser){
		code = code.substring(1, code.length()-1);
		code = ExpressionPuller.pullLists(code, sup, parser);
		if(code.contains(".."))
			return makeForLazyRangeList(code, sup);
		
		else if(code.contains("|")){
			if(code.contains("->")){}
			else{
				return makeFunctionList(code, sup, parser);
			}
		}
		
		return makeForStandardArrayList(code, sup, parser);
	}

	private static QBFunction makeFunctionList(String code, QBNamespace sup, QBParser parser) {
		String[] split = code.split("\\|");
		
		if(split.length != 2)
			throw new ExecutionException("Error parsing function list, invalid syntax, expected [index|<something with index here>]");
		
		String var = split[0].trim();
		String exp = split[1].trim();
		
		
		
		QBNamespace tmp = parser.getCurrentCompilingNamespace();
		QBFunctionListFunction func = new QBFunctionListFunction();
		parser.setCurrentCompilingNamespace(func);
		
		ByteOperation[] compiled = compiler.compile(exp);
		
		parser.setCurrentCompilingNamespace(tmp);
		func.setVar(var);
		func.setCommand(compiled);
		
		QBFunction ret =  new QBFunctionListConstructor(func);
		
		if(sup != null)
			ret.setSuper(sup);
		
		return ret;
	}

	private static QBFunction makeForStandardArrayList(String code,
			QBNamespace sup, QBParser parser) {
		QBFunction ret;
		//		System.out.println("CODE " + code);
				if(code.length() > 0){
					String[] exps = splitComma(code);
			//		System.out.println("EXPS: " + Arrays.toString(exps));
					
					ByteOperation[][] operations = new ByteOperation[exps.length][];
					
					for(int i = 0;i < exps.length;i++){
						exps[i] = ExpressionPuller.pullLists(exps[i], sup, parser);
						operations[i] = compiler.compile(exps[i]);
					}
					
					ret =  new QBArrayListConstructor(operations);
				} else{
					ret = new QBArrayListConstructor(new ByteOperation[][]{});
				}
				if(sup != null)
					ret.setSuper(sup);
				
				return ret;
	}
	
	// NOW THE BRACKETS CAN BE CUT
	private static QBFunction makeForLazyRangeList(String code, QBNamespace sup){
		String[] split = code.split("\\s*\\.\\.\\s*");
		
		if(split.length > 2 || split.length < 1)
			throw new ExecutionException("Error executing, syntax error on list");
		
		String[] change = splitComma(split[0]);
		String num;
		
		if(split.length > 1)
			num = split[1];
		else
			num = "";
		
		//System.out.println("SPLIT: " + Arrays.toString(split) + "\nCHANGE: " + Arrays.toString(change) + "\nNUM: " + num);
		
		ByteOperation[] start = compiler.compile(change[0]);
		ByteOperation[] second = change.length > 1 ? compiler.compile(change[1]) : null;
		ByteOperation[] end = num.length() > 0 ? compiler.compile(num) : null;
		
		QBFunction ret = new QBLazyRangeListConstructor(start, second, end);
		
		if(sup != null)
			ret.setSuper(sup);
		
		return ret;
	}
	
	private static final String[] splitComma(String str){
		int bracket = 0;
		int brace = 0;
		int paren = 0;
		ArrayList<String> ret = new ArrayList<String>();
		
		StringBuffer buf = new StringBuffer();
		for(int i = 0;i < str.length();i++){
			if(str.charAt(i) == ','){
				if( bracket == paren && brace == paren && paren == 0){
					ret.add(buf.toString());
					buf = new StringBuffer();
				} else{
					buf.append(str.charAt(i));
				}
			} else{
				switch((int)str.charAt(i)){
				case (int)'[':
					bracket ++;
					break;
				case (int)'{':
					brace++;
					break;
				case (int)'(':
					paren ++;
					break;
				case (int)']':
					bracket --;
					break;
				case (int)'}':
					brace--;
					break;
				case (int)')':
					paren --;
				}
				
				buf.append(str.charAt(i));
			}
		}
		ret.add(buf.toString());
		
		return ret.toArray(new String[ret.size()]);
	}
}
