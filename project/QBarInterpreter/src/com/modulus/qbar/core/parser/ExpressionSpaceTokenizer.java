package com.modulus.qbar.core.parser;

import java.util.ArrayList;
import java.util.List;

public class ExpressionSpaceTokenizer {
	public static String[] tokenize( String str ){
		// String Buffer that builds each part of the array
		StringBuffer cur = new StringBuffer();
		
		// the list that will  be returned in the end
		List<String> ret = new ArrayList<String>();
		
		// the level of the parenthesis
		int parenLevel = 0;
		
		// loop through the string one by one to split the sections
		for( int i = 0;i < str.length(); i++){
			Character ch = str.charAt(i);
			// check to see if the character is a whitespace char or not
			boolean whitespace = Character.isWhitespace(ch);
			
			// see if the character is a symbol or not
			boolean symbol = isSymbol(ch) ;
			
			// this will tell the parser if there is a paren that closes a statement
			boolean close = false;
			
			// If the character is '(' then we increase the parenthasis
			if(ch == '('){
				parenLevel ++;
			} else if(str.charAt(i) == ')'){
				// if it is ')' then mark a closing statement (This will make sense at the end)
				close = true;
			} 
			
			// if the string is one of these things, then it is time
			// to split the string.
			if((whitespace || symbol) && parenLevel == 0){
				
				// the current string is cut and trimmed
				String curstr = cur.toString().trim();
				
				// forget adding the string if it is empty
				if(curstr.length() > 0)
					ret.add(curstr);
				
				// reset the buffer.
				cur = new StringBuffer();
				
				// If the character is a symbol, add all following symbols as well
				if(symbol){
					
					// add all following symbols
					while(isSymbol(ch) && i < str.length()){
					//	System.out.println("CHAR "+ch);
						cur.append(ch);
						i++;
						ch = str.charAt(i);
					}
					
					// a little redundant, but necessary
					if(ch == '('){
						parenLevel ++;
					} else if(str.charAt(i) == ')'){
						close = true;
					} 
					
					curstr = cur.toString().trim();
					if(curstr.length() > 0)
						ret.add(curstr);
					cur = new StringBuffer();
				}
				
				// finally add the current character
				cur.append( ch );
			} else{
				cur.append( str.charAt(i) );
			}
			
			if(parenLevel < 0){
				throw new RuntimeException("Expected '('");
			}
			
			// here is the part where the close boolean comes in
			if(close)
				parenLevel --;
			
			
		}
		
		if(parenLevel > 0)
			throw new RuntimeException("Expected ')'");
		
		String curstr = cur.toString().trim();
		
		if(curstr.length() > 0)
			ret.add(curstr);
		
		return ret.toArray(new String[ret.size()]);
	}
	
	private static boolean isSymbol( char ch ){
		return !Character.isDigit(ch) && !Character.isLetter(ch) && !Character.isWhitespace(ch) && ch != '(' && ch != ')' && ch != '.' && ch != '$';
	}
}
