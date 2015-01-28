package com.modulus.qbar.core;

import com.modulus.access.ArrayFunction;
import com.modulus.common.collections.Stack;
import com.modulus.qbar.core.parser.ByteOperation;

public abstract class QBFunction extends QBObject implements ArrayFunction< QBObject, QBObject >{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7422585311092161908L;

	private boolean global;
	
	public static final QBStruct functionStruct = new QBStruct("function"){
		/**
		 * 
		 */
		private static final long serialVersionUID = -2742542386017721944L;

		@Override
		public QBObject newInstance() {
			return this;
		}
	};
	
	protected QBObject returnedValue;
	
	private int argc;
	
	public QBFunction( int argc ) {
		super(functionStruct);
		this.argc = argc;
	}
	
	public int getArgc(){
		return argc;
	}
	
	public String getName(){
		return this.toString();
	}
	
	@Override
	public abstract QBObject execute(QBObject[] args);
	
	protected void setGlobal( boolean global ){
		this.global = global;
	}
	
	protected void setArgc( int argc ){
		this.argc = argc;
	}
	
	public boolean isGlobal(){
		return global;
	}
	
	/**
	 * Relatively low level function call that pushes the object
	 * it returned onto the stack.
	 * @param func the function to call
	 * @param stack the stack to call it on
	 */
	public static void callFunction( QBFunction func, Stack<QBObject> stack ){
		int argc = func.getArgc();
		QBObject[] nextArgs = new QBObject[ argc ];
		
		for( int i = 0; i < argc; i ++ ){
			//System.out.println(func + " argc: " + argc + " i: " + i );
			nextArgs[ argc - i - 1 ] = stack.pop();
		}
		
		stack.push( func.execute(nextArgs) );
	}
	
	/*
	 * Very, very low level computation, deals with all
	 * of the individual statements that need to be executed.
	 */
	public void exec( ByteOperation[] arr, Stack<QBObject> stack){
//		System.out.println(Arrays.toString(arr));
		if(arr == null)
			return;
		
		for( ByteOperation op : arr ){
			switch( op.getOperator() ){
			case CALLG:
				//System.out.println(op.getArg().toString());
				callFunction( (QBFunction)this.get(op.getArg().toString()), stack);
				break;
			case CALL:
				QBObject caller = stack.peek();
				
				if(caller instanceof QBUtilityNamespace)
					stack.pop();
				//System.out.println("CALL " + caller);
				callFunction( (QBFunction)caller.get(op.getArg().toString()), stack);
				break;
			case PUSH:
				String[] str = (String[]) op.getArgs();
				
				stack.push( this.hardGet(str) );
				break;
			case PUSHO:
				QBObject obj = (QBObject) op.getArg();
				stack.push(obj);
				break;
				
			case STO:
				QBObject pop = stack.peek();
				this.hardSet((String[]) op.getArgs(), pop);
				break;
				
			case ITR:
				QBObject popped = stack.peek();
				String name = op.getArg().toString();
				stack.push(popped.iterator(name, this));
				
			}
		}
		
		// this method does not return via the stack, but pops the value
		// into a holder to help prevent stack memory leaks.
		returnedValue = stack.pop();
	}
	
	public QBObject getReturnedValue(){
		return returnedValue;
	}
}
