package com.modulus.qbar.core.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.modulus.common.collections.MArrays;
import com.modulus.dataread.expressions.ExpressionParser;
import com.modulus.dataread.expressions.ExpressionPart;
import com.modulus.qbar.core.interpreter.QBInterpreter;
import com.modulus.qbar.lang.QBList;

/**
 * This class is most of the guts for compiling the source code
 * into a hybrid of interpreted and byte code, this is used to promote
 * a high degree of efficiency as well as maintain a high level
 * of programming.
 * 
 * From here, the resulting code can then be fed into an interpreter
 * to be interpreted using a stack and heap, like most of the other
 * programming languages.
 * 
 * This class is the main part of the language.
 * 
 * @author jrahm
 *
 */
public class QBExpressionParser implements ExpressionParser{
	private static Map<Integer, List<String> > operatorPrecedence = new HashMap<Integer, List<String>>();
	
	/*
	 * Need to puting some of the `natural` functions
	 * with their respective precedence related to one and other.
	 */
	static{
		QBList.init();
		mapOp("#", 9); // POW
		mapOp("as", 9); // POW
		mapOp("isa", 9); // POW
		
		mapOp("^", 10); // POW
		mapOp("*", 20); // MUL
		mapOp("/", 20); // DIV
		mapOp("div", 20); // DIVI
		mapOp("%", 20); // MOD
		mapOp("+", 30); // ADD
		mapOp("++", 30); // ADD
		mapOp("-", 30); // SUB
		
		mapOp("<", 128); // STO
		mapOp(">", 128); // STO
		mapOp("<=", 128); // STO
		mapOp(">=", 128); // STO
		mapOp("==", 128); // STO
		mapOp("!=", 128); // STO
		
		mapOp("not", 512); // STO
		mapOp("xor", 512); // STO
		mapOp("or", 512); // STO
		mapOp("and", 512); // STO
		
		mapOp("<-", 1024); // STO
		mapOp("->", 1024); // STO
	}

	public static void main(String[] args){
		QBExpressionParser parser = new QBExpressionParser();
		ExpressionPart exp = parser.parseExpressions("4 + 5 - (if (5+1) then (4+2) else (3+3))");
		System.out.println(exp.compilePart());
	}
	
	@Override
	public ExpressionPart parseExpressions(String exp) {
		String[] strs = ExpressionSpaceTokenizer.tokenize(exp);
		return parseExpressions(strs, QBInterpreter.instance().getParser() );
	}
	
	/**
	 * This function further parses from a String[], unlike its
	 * counterpart which uses just a String to parse.
	 * 
	 * @param exp the expression to parse
	 * @return an ExpressionPart that represents this code
	 */
	public ExpressionPart parseExpressions (String[] exp, QBParser parser) {
		exp = fixArray(exp);
	//	System.out.println("EXPS: " + Arrays.toString(exp));
		if(exp[0].startsWith("if")){
			if( (exp[0].length() > 2 && !Character.isLetterOrDigit( exp[0].charAt(2) )) || exp[0].length() == 2){
				String name = ExpressionPuller.pullIfThenElse(exp, parser.getCurrentCompilingNamespace());
				exp = new String[]{name};
			}
		}
		
//		if(exp[0].equals("if")){
//			int thenIdx = MArrays.indexOf(exp, "then");
//			String ifPart = MArrays.concat(exp, 1, thenIdx-1);
//			
//			int elseIdx = MArrays.indexOf(exp, "else");
//			String thenPart = MArrays.concat(exp, thenIdx+1, elseIdx-thenIdx-1);
//			
//			String elsePart = MArrays.concat(exp, elseIdx+1);
//			
//			ExpressionPart ifExpPart = parseExpressions(ifPart);
//			ExpressionPart thenExpPart = parseExpressions(thenPart);
//			ExpressionPart elseExpPart = parseExpressions(elsePart);
//			
//			ExpressionPart totalIf = ExpressionPart.makePrimaryExpressionPart(null, "if", new ExpressionPart[]{ifExpPart});
//			ExpressionPart totalThen = ExpressionPart.makePrimaryExpressionPart(totalIf, "then", new ExpressionPart[]{thenExpPart});
//			ExpressionPart total = ExpressionPart.makePrimaryExpressionPart(totalThen, "else", new ExpressionPart[]{elseExpPart});
//		
//			return total;
//		}
		

		// Go though all of the operators in order from their respective order of operations.
		for( String[] ops : operators() ){
			
			int idx = -1;
			// Some operators have the same precedence, so we need to deal with that
			for( String op : ops ) {
				idx = Math.max(idx, MArrays.lastIndexOf(exp, op));
			}
			
			// if we found one of these opeartors, then we
			// need to deal with it.
			if( idx != -1 ){
				// get the function (thats easy, it is what we just found)
				String function = exp[idx];
				
				// get all stuff before and after this point
				// the first is the caller and the second is the
				// argument believe me it works because
				// 3 * 4 / 6 + 1 * 2 - 3 ^ 2 * 2 = (3 * 4 / 6 + 1 * 2) - (3 ^ 2 * 2)
				String[] before = Arrays.copyOfRange(exp, 0, idx);
				String[] after = Arrays.copyOfRange(exp, idx + 1, exp.length);
				
				// now, using recursion we make the expression parts out of them
				ExpressionPart part1 = parseExpressions( before, parser );
				ExpressionPart part2 = parseExpressions( after, parser );
				
				if(function.equals("->")){
					ExpressionPart tmp = part1;
					part1 = part2;
					part2 = tmp;
				}
				// now put the whole thing together, and you now have a mess
				return ExpressionPart.makePrimaryExpressionPart(part1, function, new ExpressionPart[]{part2} );
			}
				
			
		}
		
		// If there are no more "Binary Functions"
		// what is left must be grouped or it must be a function
		if( exp.length == 1 ){
			String str = exp[0];
			if( completelySurrounded(str) ){
				String middle = str.substring(1, str.length() - 1);
				return parseExpressions(middle);
			}
			// This must then be a function
			else if(str.endsWith(")")){
				
				// this function will get the last index of a dot ('.') that is outside parenthesis
				// the dot is used to determine if the function is being called by an object
				int dotIdx = lastDotIndex(str);
				
				// now we find the index that the arguments start on
				int idx = str.indexOf('(',dotIdx);
				
				// check to make sure there is one.
				if( idx < 0 ){
					throw new RuntimeException("Expected '('");
				}
				
				// from here it is pretty straight forward to get the function,
				// caller and parameters.
				String function = str.substring(0, idx);
				String args = str.substring(idx + 1, str.length() - 1);
				String caller = null;
				
				// if there is not dot, then there is no caller and the function is global
				if( dotIdx >= 0 ){
					caller = function.substring( 0, dotIdx );
					function = function.substring( dotIdx + 1 );
				}
				
				// now use recursion to build the parts and make a new part.
				ExpressionPart part1 = caller !=null ? parseExpressions(caller) : null;
				ExpressionPart[] parts = parseParams(args);
				
				return ExpressionPart.makePrimaryExpressionPart(part1, function, parts);
				
			}
			
			else{
				// This is the base case, the very bottom of the ladder, this is where all
				// that is left is some entity that has no function.
				return ExpressionPart.makeExpressionPart(exp[0]);
			}
		}
		
		throw new RuntimeException("Syntax Error");
	}
	
	private ExpressionPart[] parseParams( String params ){
		params = params.trim();
		if(params.length() == 0)
			return new ExpressionPart[]{};
		
		String[] split = splitParam(params);
		ExpressionPart[] parts = new ExpressionPart[split.length];
		
		for ( int i = 0;i < split.length; i++ ){
			parts[i] = parseExpressions(split[i]);
		}
		
		// Might need this
		//Collections.reverse( Arrays.asList(parts) );
		return parts;
	}
	
	private static String[] splitParam( String params ){
		int paramIdx = 0;
		StringBuffer cur = new StringBuffer();
		List<String> ret = new ArrayList<String>();

		for( int i = 0;i < params.length(); i++){
			char ch = params.charAt(i);
			if(ch == '(')
				paramIdx ++;
			
			else if( ch == ')' )
				paramIdx --;
			
			if( paramIdx < 0 )
				throw new RuntimeException("Expected '('");
			
			if( ch == ',' && paramIdx == 0 ){
				ret.add(cur.toString());
				cur = new StringBuffer();
			} else{
				cur.append(ch);
			}
		}
		
		ret.add(cur.toString());
		
		return ret.toArray(new String[ret.size()]);
	}
	
	private String[] fixArray(String[] exp){
		List<String> arr = new ArrayList<String>();
		boolean last = true;
		for(int i = 0;i < exp.length;i ++){
			if( isDigit(exp[i]) ){
				last = true;
			} else{
				if(!last && exp[i].equals("-")){
					exp[i+1] = "-" + exp[i+1];
					exp[i] = "";
				}
				
				last = false;
			}
			if(exp[i].length() > 0)
				arr.add(exp[i]);
		}
		
		return arr.toArray(new String[arr.size()]);
	}
	
	private boolean isDigit(String exp){
		return Pattern.matches("(\\d|\\w|\\.)+",exp);
	}
	
	public static void mapOp(String op, Integer prec){
		List<String> lst = operatorPrecedence.get(prec);
		
		if(lst == null){
			lst = new ArrayList<String>();
			operatorPrecedence.put(prec, lst);
		}
		
		lst.add(op);
	}
	
	private static List<String[]> operators(){
		List<String[]> lst = new ArrayList<String[]>();
		Collection<Integer> keys = operatorPrecedence.keySet();
		List<Integer> keyList = new ArrayList<Integer>();
		
		keyList.addAll(keys);
		Collections.sort(keyList);
		Collections.reverse(keyList);
		
		for (Integer i : keyList){
			List<String> temp = operatorPrecedence.get(i);
			lst.add( temp.toArray( new String[temp.size()] ));
		}
		
		return lst;
	}
	
	private static boolean completelySurrounded( String str ){
		int paramIdx = 0;
		for( int i = 0;i < str.length(); i++ ){
			if(str.charAt(i) == '(')
				paramIdx ++;
			else if (str.charAt(i) == ')')
				paramIdx --;
			
			if( paramIdx == 0 )
				return false;
		}
		
		return true;
	}
	
	private static int lastDotIndex( String str ){
		int paramIndex = 0;
		
		for( int i = str.length()-1;i >= 0;i--){
			if(str.charAt(i) == ')')
				paramIndex --;
			else if(str.charAt(i) == '(')
				paramIndex ++;
			else if(str.charAt(i) == '.' && paramIndex == 0)
				return i;
		}
		
		return -1;
	}

}
