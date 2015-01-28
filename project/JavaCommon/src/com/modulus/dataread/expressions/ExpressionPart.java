package com.modulus.dataread.expressions;

/**
 * This class is used to help to assemble the code into more
 * readable code for the computer, it sets the system based on
 * a more of a stack-ish way so the virtual machine can linearly
 * push/pop arguments and call functions.
 * 
 * @author jrahm
 *
 */
public abstract class ExpressionPart {
	
	// Null if function is global
	private ExpressionPart caller;
	private ExpressionPart[] args;
	
	private String function;
	private boolean primary = false;
	
	private ExpressionPart(){}
	
	/**
	 * This creates a new Expression part meant to handle multi-part
	 * expressions such as expressions with parenthesis, functions 
	 * inside of functions and so on.
	 * 
	 * Q-Bar is set up to be very functional, to the point where every entity is made
	 * of functions being called on arguments and such.
	 * 
	 * @param caller the caller of the function (null if the function is global)
	 * @param function the function to be called
	 * @param args the arguments of the function
	 * @return an expression part that describes this layout
	 */
	public static ExpressionPart makePrimaryExpressionPart( ExpressionPart caller, String function, ExpressionPart[] args ){
		ExpressionPart ths = new ExpressionPart(){
			private String compile;
			@Override
			public String compilePart() {
				
				if( compile == null ){
					StringBuffer buf = new StringBuffer();
					ExpressionPart caller = this.getCaller();

				//	else
				//		buf.append( "$global$" );
					for(ExpressionPart part : this.getArgs()){
						buf.append(' ');
						buf.append( part.compilePart() );
					}
					
					if(caller != null)
						buf.append( " " + caller.compilePart() );
					
					buf.append(' ');
					if(this.getFunction().trim().length() > 0){
						if(caller != null){
							// \u00FF is the marker for what is an instance function
							buf.append(  "\u00FF" + this.getFunction() );
						}
						else{
							// \u00FE is the marker for a global function
							buf.append( "\u00FE" + this.getFunction() );
						}
					}
					compile = buf.toString().replaceAll("\\s+", " ");
				}
				
				return compile;
			}
		};
		
		ths.caller = caller;
		ths.function = function;
		ths.args = args;
		
		ths.primary = true;
		
		return ths;
	}
	
	/**
	 * This method returns an expression part that is made entirely up
	 * of one single variable name. On a side note, these parts are the
	 * recursive base case for the compilation.
	 * 
	 * The name of the variable is stored in the function value, as a variable
	 * name can be thought of as a function that returns that point in
	 * memory.
	 * 
	 * @param name the name of the variable.
	 * @return A base case ExpressionPart
	 */
	public static ExpressionPart makeExpressionPart( String name ){
		ExpressionPart ths = new ExpressionPart(){
			@Override
			public String compilePart() {
				return this.getFunction();
			}
		};
		ths.function = name;
		
		return ths;
	}
	
	/**
	 * @return the caller
	 */
	public ExpressionPart getCaller() {
		return caller;
	}

	/**
	 * @return the args
	 */
	public ExpressionPart[] getArgs() {
		return args;
	}

	/**
	 * @return the function
	 */
	public String getFunction() {
		return function;
	}

	/**
	 * @return the primary
	 */
	public boolean isPrimary() {
		return primary;
	}

	public abstract String compilePart();
}
