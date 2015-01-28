package com.modulus.qbar.core;

import java.util.HashMap;
import java.util.Map;

import com.modulus.common.collections.ArrayStack;
import com.modulus.common.collections.Stack;
import com.modulus.dataread.expressions.FlowStatement;
import com.modulus.dataread.expressions.Statement;
import com.modulus.qbar.core.exceptions.ParseException;
import com.modulus.qbar.core.interpreter.QBInterpreter;
import com.modulus.qbar.core.parser.ByteOperation;
import com.modulus.qbar.core.parser.QBParser;
import com.modulus.qbar.core.parser.stmt.QBStatement;
import com.modulus.qbar.core.primitive.QBPrimitive;
import com.modulus.qbar.lang.QBIterator;

public class QBSyntheticFunction extends QBFunction{
	private Stack<QBNamespace> namespaceCalls = new ArrayStack<QBNamespace>();
	/**
	 * 
	 */
	private static final long serialVersionUID = 2890479116324397754L;
	private QBStatement code;
	private String name;
	private String[] args;
	private Map<String, QBObject> namespaceTemplate = new HashMap<String,QBObject>();
	private QBObject defaultThis;
	
	private Map<String, QBObject> subs = new HashMap<String, QBObject>();
	
	public QBSyntheticFunction( QBStatement code, QBParser parser ){
		super( -1 );
		this.code = code;
		
		namespaceCalls.push(new QBNamespace(namespaceTemplate));
		parse(parser);
	}
	
	public void parse(QBParser parser){
		String header = code.getHeader(); // should return header, e.g. let someFunction(a,b,c) = do
		
		if(!header.startsWith("let")){
			throw new ParseException("Invalid Syntax For Function, expected `let`", code);
		}
		
		// this means that code is a one-line function and we need to break it up
		if( !code.hasChildren() ){
			String[] arr = header.split("=", 2);
			header = arr[0] + " = ";

			QBStatement onlyChild = new QBStatement(QBParser.formatter);
			onlyChild.setLineNumber(code.getLineNumber());
			onlyChild.setHeader(arr[1]);
			
		
			code.addChild(onlyChild);
			code.setHeader(header);
		}
		
		this.args = chompArgs( header );
		this.name = chompName( header );
		
		for( Statement stmt : code.getChildren() ){
			String expression = stmt.getHeader();
			
			//System.out.println("EXPPRS: "+expression);
			
			stmt.setHeader(expression);
			
			parser.readStatement( stmt, this );
		}
	}
	
	/*
	 * Return the args of a function
	 */
	private String[] chompArgs(String header){
	//	System.out.println(header);
		int idx1 = header.indexOf('(');
		int idx2 = header.lastIndexOf(')');
		
		if(idx1 == idx2 && idx1 == -1){
			return new String[]{"this"};
		} else if( idx1 == -1 || idx2 == -1){
			throw new ParseException("Syntax Error", code);
		}
		// should parse header, like the a,b,c part of
		// let someFunction(a,b,c) = do
		String args = header.substring( idx1 + 1, idx2 ).trim();
	
		// split the arguments into string[], trimming all unnecessary whitespace
		String[] ret = (args + ",this").split("\\s*,\\s*");
		
		
		return ret;
	}
	
	/*
	 * return the name of the function
	 */
	static private String chompName(String header){
		// given header is to the effect of, `let someFunction(a,b,c) = do`
		
		// Chop the `let` from the string
		header = header.substring(3);
		int idx = header.indexOf('(');
		if(idx == -1)
			idx = header.indexOf('=');
		// get the name
		return header.substring(0, idx).trim();
	}
	
	private boolean ifBit;
//	private QBObject returnedValue;
	
	@Override
	public QBObject execute(QBObject[] args) {
	//	System.out.println("Executing: " + this.getName());
		QBNamespace layer = new QBNamespace(new HashMap<String, QBObject>());
		layer.setSuper(this.getSuper());
		
		for( String str : subs.keySet() ) {
			QBObject clone = subs.get(str).clone();
			layer.set(str,  clone);
			clone.setSuper(layer);
		}
		
	//	System.out.println(layer.toString2());
		//System.out.println("push");
		namespaceCalls.push(layer);
		
		mapArgs( args );
		Stack<QBObject> stack = QBInterpreter.instance().getStack();
		Statement[] statements = code.getChildren();
	//	System.out.println(Arrays.toString(statements));
		execute(statements, stack);
		
		//System.out.println("pop");
		namespaceCalls.pop();
		return stack.pop();
	}
	
	/**
	 * Guts of the exection process, when it operates on this low of of level
	 */
	public void execute(Statement[] statements, Stack <QBObject> stack){
		QBStatement exe = null;
		Map<QBStatement, QBIterator> iterators = new HashMap<QBStatement, QBIterator>();
		Stack<QBStatement> toProcess = new ArrayStack<QBStatement>();
		try{
		// push all of the statements onto the stack
		// the pushAll method pushes the statements on backwards
		pushAll(statements, toProcess);

		// execute the statements
		while(!toProcess.isEmpty()){
			
			//if(toProcess.size() == 1 && namespaceCalls.size() == 2)
			//	System.out.println("Processing: " + toProcess.peek().getHeader() + " :: " + namespaceCalls.size() );
			// get the next statement to execute
			exe = toProcess.peek(); // look at the statement
		//	System.out.println("EXE " + exe + ", " + exe.getFlowStatement());
			// is there some kind of a flow statement attached with this function?
			FlowStatement flow = exe.getFlowStatement();
			
			if( flow == FlowStatement.FOR){
				QBIterator<?> iter = iterators.get(exe);
				
				if(iter == null){
					ByteOperation[] ops = exe.getOperations();
					exec(ops, stack);
					iter = (QBIterator<?>) returnedValue;
					
					iterators.put(exe, iter);
				}
				
				if(!iter.hasNext()){
					toProcess.pop();
					iterators.put(exe, null);
				} else{
					iter.next();
					pushAll( exe.getChildren(), toProcess );
				}
			}
			// if and whiles are very similar, the only difference being that one repeats and the other does not
			else if(flow == FlowStatement.IF || flow == FlowStatement.WHILE){
				
				// if statements do not repeat
				if(flow == FlowStatement.IF)
					toProcess.pop(); // pop it off to make it not repeat.
				
				// these are the operations
				ByteOperation[] ops = exe.getOperations();
				
				// execute the operations
				exec(ops, stack);
				
				// the method exec pops what it returns in returnedValue;
				QBObject obj = returnedValue;
				
				// check to see if the boolean evaluated to true
				if( obj instanceof QBPrimitive &&((QBPrimitive) obj).intValue() > 0){ // TODO fix this to use method in Core
					pushAll( exe.getChildren(), toProcess );
					ifBit = true;
				}
				// if not
				else{
					// when the while statement is false, it is time to move on
					if(flow == FlowStatement.WHILE){
						toProcess.pop();
					} else{
						ifBit = false;
					}
				}
			} else if( flow == FlowStatement.ELSE){
				// just pop it
				toProcess.pop();
				
				// if the if bit was false
				if( !ifBit ){
					
					// push all the children
					pushAll( exe.getChildren(), toProcess );
				}
			}
			// if there is a return statement stop right now
			else if ( flow == FlowStatement.RETURN ){
				toProcess.pop();
				exec( exe.getOperations(), stack );
				
				stack.push( returnedValue );
				return;
			}
			// if there is no flow, then just pop and execute.
			else {
				toProcess.pop();
				exec( exe.getOperations(), stack );
			}
		}
		
		// if this is the end, then it is time just to return the value that was executed by the last command,
		// this means that if there is no return statement, then the last command becomes the return statement.
		stack.push( returnedValue );
		} catch(RuntimeException e){
			e.printStackTrace();
			throw new ParseException(e.getMessage(), e, exe);
		}
	}
	
	/*
	 * This function pushes all of the elements in an array
	 * onto the stack in reverse order.
	 */
	private void pushAll( Statement[] arr, Stack<QBStatement> toProcess ){
		for ( int i = arr.length - 1; i >= 0; i --){
			toProcess.push( (QBStatement) arr[i]);
		}
	}
	
	
	

	
	
	private void mapArgs(QBObject[] args){

		for( int i = 0;i < args.length; i ++){
			this.set(this.args[i], args[i]);
		}
		
		if( defaultThis != null )
			this.set("this", defaultThis);
	}
	
	@Override
	public int getArgc(){
		return this.args.length;
	}
	
	@Override
	public String getName(){
		return name;
	}
	
	public String toString2(){
		return code.toString();
	}
	
	@Override
	public void setGlobal( boolean global ){
		super.setGlobal(global);

		if( global ){
			if(args[args.length - 1].equals("this")){
				String[] next = new String[args.length - 1];
				System.arraycopy(this.args, 0, next, 0, args.length - 1);
				args = next;
			} else if(!args[args.length - 1].equals("this")){
				String[] next = new String[args.length + 1];
				System.arraycopy(this.args, 0, next, 0, args.length);
				next[args.length] = "this";
				args = next;
			} 
		}

	}
	
	@Override
	public void setSuper( QBNamespace space){
		super.setSuper(space);
	//	System.out.println("Setting Space To " + space);
		
		if( space instanceof QBObject && !(space instanceof QBUtilityNamespace)){
			setGlobal(false);
		} else {
			setGlobal(true);
		}
		
		if(space instanceof QBUtilityNamespace){
			this.defaultThis = (QBObject) space;
		}
	}
	
	@Override
	public QBObject get(String name){
		return namespaceCalls.peek().get(name);
	}
	
	@Override
	public void set(String name, QBObject obj){
		namespaceCalls.peek().set(name, obj);
	}
	
	public void addSubObject( String name, QBObject obj ){
		this.subs.put(name, obj);
	}
}
