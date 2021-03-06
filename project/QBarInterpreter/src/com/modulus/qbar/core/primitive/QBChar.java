package com.modulus.qbar.core.primitive;

import com.modulus.qbar.core.QBObject;
import com.modulus.qbar.core.QBStruct;

/**
 * Class that represents the
 * Q-Bar primitive value char
 */
 /*
  * this class was generated by the python script
  * 'generate_primitives.py
  * @PythonGenerated
  */
public class QBChar extends QBPrimitive{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3811922090207258658L;
	private char value;
    public final static QBStruct struct = new QBStruct("Character", QBPrimitive.struct) {
		
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
    /**
     * Constructs a new QBarChar that wraps
     * the type char
     */
    public QBChar( char value ){
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
    	return Character.toString(value);
    }
    
    public Number getValueNumber(){
    	return (int)value;
    }
    
    @Override
    public Object getWrapped(){
    	return value;
    }
}
