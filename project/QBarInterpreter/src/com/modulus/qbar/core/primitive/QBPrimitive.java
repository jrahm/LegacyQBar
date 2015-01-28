package com.modulus.qbar.core.primitive;

import java.math.BigDecimal;

import com.modulus.qbar.core.NaturalFunctions;
import com.modulus.qbar.core.QBObject;
import com.modulus.qbar.core.QBStruct;
import com.modulus.qbar.core.exceptions.ExecutionException;

/**
 * Class that describes very natural and primitive
 * objects in the Q-Bar language.
 * 
 * @author jrahm
 *
 */
public abstract class QBPrimitive extends QBObject implements Comparable<QBPrimitive>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3882060949668211170L;
	public static final QBStruct struct = new QBStruct("Primitive") {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -93702214222801325L;

		@Override
		public QBObject newInstance() {
			// TODO Auto-generated method stub
			return null;
		}
	};
	
	static{
		struct.set("+", NaturalFunctions.ADD);
		struct.set("-", NaturalFunctions.SUB);
		struct.set("*", NaturalFunctions.MUL);
		struct.set("%", NaturalFunctions.MOD);
		struct.set("/", NaturalFunctions.DIV);
		struct.set("^", NaturalFunctions.POW);
		
		struct.set(">", NaturalFunctions.GT);
		struct.set("<", NaturalFunctions.LT);
		struct.set("==", NaturalFunctions.EQ);
		struct.set("!=", NaturalFunctions.NE);
		struct.set(">=", NaturalFunctions.GTE);
		struct.set("<=", NaturalFunctions.LTE);
		
		struct.set("add", NaturalFunctions.ADD);
		struct.set("subtract", NaturalFunctions.SUB);
		struct.set("multiply", NaturalFunctions.MUL);
		struct.set("modulo", NaturalFunctions.MOD);
		struct.set("divide", NaturalFunctions.DIV);
		struct.set("power", NaturalFunctions.POW);
		
		struct.set("greateThan", NaturalFunctions.GT);
		struct.set("lessThan", NaturalFunctions.LT);
		struct.set("equalTo", NaturalFunctions.EQ);
		struct.set("notEqualTo", NaturalFunctions.NE);
		struct.set("greaterThanOrEqualTo", NaturalFunctions.GTE);
		struct.set("lessThanOrEqualTo", NaturalFunctions.LTE);
		
		struct.set("as", NaturalFunctions.AS);
	}
	/**
	 * Constructs a simple new QBPrimitive object
	 */
	public QBPrimitive() {
		super( struct );
	}
	
	protected QBPrimitive( QBStruct struct ){
		super(struct);
	}
	
	@Override
	public void set(String str, QBObject obj){
		throw new ExecutionException("Attempting to assign primitive member");
	}
	
	protected void setSafe( String str, QBObject obj ){
		super.set(str, obj);
	}
	
	/**
	 * Gets the integer value of this primitive value
	 * @return the integer value of this primitive value;
	 */
	public abstract int intValue();
	
	/**
	 * Gets the double value of this primitive value
	 * @return the double value of this primitive value
	 */
	public abstract double doubleValue();
	
	/**
	 * Gets the char value of this primitive value
	 * @return the char value of this primitive value
	 */
	public abstract char charValue();
	
	/**
	 * Returns the value of this QBPrimitive as a number
	 * @return the value of this QBPrimitive as a number
	 */
	public abstract Number getValueNumber();
	
	@Override
	public int compareTo(QBPrimitive other){
		return (int) Math.signum( this.getValueNumber().doubleValue() - other.getValueNumber().doubleValue() );
	}
	
	public static QBPrimitive forNumber(Number num){
		if( num instanceof Double || num instanceof Float || num instanceof BigDecimal )
			return new QBDouble(num.doubleValue());
		
		return new QBInt(num.intValue());
	}
}
