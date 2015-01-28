package com.modulus.qbar.core.parser;

import java.util.ArrayList;
import java.util.List;

import com.modulus.common.collections.MArrays;
import com.modulus.qbar.core.QBFunction;
import com.modulus.qbar.core.QBNamespace;
import com.modulus.qbar.core.interpreter.QBInterpreter;
import com.modulus.qbar.lang.QBString;

public class ExpressionPuller {
	public static void main(String[] args){
		String str = "\"Hello, World!\"";
		
		str = pullStrings(str);
		System.out.println(str);
	}
	
	public static String pullLists(String expression, QBNamespace sup, QBParser parser){
		String tmp;
		
		while(!(tmp = replaceOneList(expression, sup, parser)).equals(expression))
			expression = replaceOneList(tmp, sup, parser);
		
		return expression;
	}
	
	public static String pullStrings(String expression){
		List<Integer> points = new ArrayList<Integer>();
		points.add(0);

		for(int i = 0;i < expression.length();i++){
			if(expression.charAt(i) == '"' && !(i > 0 && expression.charAt(i-1) == '\\') ){
				int stamp = i;
				i++;
				while(expression.charAt(i) != '"'){
					if(expression.charAt(i) == '\\')
						i++; // skip the next character
					i++;
				}
				
				points.add( stamp );
				points.add( i+1 );
			}
		}
		points.add( expression.length() );
		
		StringBuffer total = new StringBuffer();
		boolean inString = false;
		for(int i = 1;i < points.size();i++){
			int i1 = points.get(i-1);
			int i2 = points.get(i);
		//	System.out.println("("+i1+", "+i2+")");
			if(!inString){
				total.append( expression.substring(i1, i2) );
			} else{
				String exp = expression.substring(i1+1, i2-1);
				int hash = exp.hashCode();
				
				String name = "$string$"+Math.abs(hash);
				total.append(name);
				
				QBString str = new QBString(exp, true);
				QBInterpreter.instance().getGlobalNamespace().set(name, str);
			}
			
			inString = !inString;
		}
		
		return total.toString();
	}
	
	public static String pullIfThenElse( String[] exp, QBNamespace sup ){
		exp = fixIfThenElseParts(exp);
		
		
		int thenIdx = MArrays.indexOf(exp, "then");
		String ifPart = MArrays.concat(exp, 1, thenIdx-1, " ");
		
		int elseIdx = MArrays.indexOf(exp, "else");
		String thenPart = MArrays.concat(exp, thenIdx+1, elseIdx-thenIdx-1, " ");
	//	System.out.println("Pull If Then Else EXP: " + Arrays.toString(exp) + " $ " + elseIdx);
		String elsePart = MArrays.concat(exp, elseIdx+1, " ");
		
		QBFunction func = IfThenElseFunctionFactory.makeIfThenElse(ifPart, thenPart, elsePart);
		
		if(sup != null)
			func.setSuper(sup);
		
		String name = "$if$then$else"+ Math.abs((ifPart+elsePart+thenPart).hashCode());
		QBInterpreter.instance().getGlobalNamespace().set(name, func);
		
		return name + "()";
	}
	
	public static String replaceOneList(String exp, QBNamespace sup, QBParser parser){
		if(!exp.contains("["))
			return exp;
		
		int off = exp.indexOf('[') + 1;
		off = off == 0 ? 1 : off;
		
		int bracket = 1;
		int i;
		
		for(i = off;bracket != 0 && i < exp.length();i++){
			if(exp.charAt(i) == '[')
				bracket ++;
			else if(exp.charAt(i) == ']')
				bracket --;
		}
		
		String start = exp.substring(0, off-1);
		String end = exp.substring(i);
		
		String middle = exp.substring(off-1, i);
		
		int hash = Math.abs(middle.hashCode());
		String name = "$list$constructor$"+hash;
		
		QBFunction func = ListConstructorFactory.makeNewQBListConstructor(middle, sup, parser);
		QBNamespace global = QBInterpreter.instance().getGlobalNamespace();
		
		global.set(name, func);
	
		return start+ name + "()" + end;
	}
	
	private static String[] fixIfThenElseParts(String[] ifthenelse){
		ArrayList<String> strs = new ArrayList<String>();
		
		for(String s : ifthenelse){
			if(s.startsWith("if") && s.length() > 2 && !Character.isLetterOrDigit(s.charAt(2))){
				String str1 = s.substring(0,2);
				String str2 = s.substring(2);
				
				strs.add(str1);
				strs.add(str2);
			} else if(s.startsWith("then") && s.length() > 4 && !Character.isLetterOrDigit(s.charAt(4))){
				String str1 = s.substring(0,4);
				String str2 = s.substring(4);
				
				strs.add(str1);
				strs.add(str2);
			} else if(s.startsWith("else") && s.length() > 4 && !Character.isLetterOrDigit(s.charAt(4))){
				String str1 = s.substring(0,4);
				String str2 = s.substring(4);
				
				strs.add(str1);
				strs.add(str2);
			} else{
				strs.add(s);
			}
		}
		
		return strs.toArray( new String[strs.size()] );
	} 

}
