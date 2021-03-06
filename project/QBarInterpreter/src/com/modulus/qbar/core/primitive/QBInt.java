package com.modulus.qbar.core.primitive;

import com.modulus.qbar.core.NaturalFunctions;
import com.modulus.qbar.core.QBObject;
import com.modulus.qbar.core.QBStruct;

/**
 * Class that represents the
 * Q-Bar primitive value int
 */
 /*
  * this class was generated by the python script
  * 'generate_primitives.py
  * @PythonGenerated
  */
public class QBInt extends QBPrimitive{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int value;
    public final static QBStruct struct = new QBStruct("Integer", QBPrimitive.struct) {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public QBObject newInstance() {
			// TODO Auto-generated method stub
			return null;
		}
	};
	public static final QBInt TRUE = new QBInt(1);
	public static final QBInt FALSE = new QBInt(0);
	static{
		struct.set("and", NaturalFunctions.AND);
		struct.set("or", NaturalFunctions.OR);
		struct.set("xor", NaturalFunctions.XOR);
		
		// just adding these to speed up the process, after all
		// integers must run as fast as possible, but these would
		// still be in the super struct.
		struct.set("+", NaturalFunctions.ADDI);
		struct.set("-", NaturalFunctions.SUBI);
		struct.set("*", NaturalFunctions.MULI);
		struct.set("%", NaturalFunctions.MODI);
		struct.set("/", NaturalFunctions.DIV);
		struct.set("div", NaturalFunctions.DIVI);
		struct.set("^", NaturalFunctions.POWI);
		
		struct.set(">", NaturalFunctions.GT);
		struct.set("<", NaturalFunctions.LT);
		struct.set("==", NaturalFunctions.EQ);
		struct.set("!=", NaturalFunctions.NE);
		struct.set(">=", NaturalFunctions.GTE);
		struct.set("<=", NaturalFunctions.LTE);
		
		struct.set("add", NaturalFunctions.ADDI);
		struct.set("subtract", NaturalFunctions.SUBI);
		struct.set("multiply", NaturalFunctions.MULI);
		struct.set("modulo", NaturalFunctions.MODI);
		struct.set("divide", NaturalFunctions.DIV);
		struct.set("power", NaturalFunctions.POWI);
		struct.set("divideInteger", NaturalFunctions.DIVI);
		
		struct.set("greaterThan", NaturalFunctions.GT);
		struct.set("lessThan", NaturalFunctions.LT);
		struct.set("equalTo", NaturalFunctions.EQ);
		struct.set("notEqualTo", NaturalFunctions.NE);
		struct.set("greaterThanOrEqualTo", NaturalFunctions.GTE);
		struct.set("lessThanOrEqualTo", NaturalFunctions.LTE);
	}
    /**
     * Constructs a new QBarInt that wraps
     * the type int
     */
    public QBInt( int value ){
    	super(struct);
        this.value = value;
    }
    
    @Override
    public int intValue(){
        return (int) this.value;
    }
    
    @Override
    public double doubleValue(){
        return (double) this.value;
    }
    
    @Override
    public char charValue(){
        return (char) this.value;
    }
    
    public String toString(){
    	return Integer.toString(value);
    }
    
    public Number getValueNumber(){
    	return value;
    }
    
    @Override
    public Object getWrapped(){
    	return value;
    }
}
