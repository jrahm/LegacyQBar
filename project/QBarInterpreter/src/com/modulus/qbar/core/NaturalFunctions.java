package com.modulus.qbar.core;

import com.modulus.qbar.core.primitive.QBChar;
import com.modulus.qbar.core.primitive.QBDouble;
import com.modulus.qbar.core.primitive.QBInt;
import com.modulus.qbar.core.primitive.QBPrimitive;

public class NaturalFunctions {
	public static final QBFunction ADD = new QBFunction(2) {	
		/**
		 * 
		 */
		private static final long serialVersionUID = -458278389954945664L;

		@Override
		public QBObject execute(QBObject[] args) {
			QBPrimitive obj2 = (QBPrimitive) args[0];
			QBPrimitive obj1 = (QBPrimitive) args[1];
			
			return new QBDouble( obj1.doubleValue() + obj2.doubleValue() );
		}
	};
	
	public static final QBFunction SUB = new QBFunction(2) {	
		/**
		 * 
		 */
		private static final long serialVersionUID = 8875619169775793806L;

		@Override
		public QBObject execute(QBObject[] args) {
			QBPrimitive obj2 = (QBPrimitive) args[0];
			QBPrimitive obj1 = (QBPrimitive) args[1];
			
			return new QBDouble( obj1.doubleValue() - obj2.doubleValue() );
		}
	};
	
	public static final QBFunction MUL = new QBFunction(2) {	
		/**
		 * 
		 */
		private static final long serialVersionUID = -5397892833871103232L;

		@Override
		public QBObject execute(QBObject[] args) {
			QBPrimitive obj2 = (QBPrimitive) args[0];
			QBPrimitive obj1 = (QBPrimitive) args[1];
			
			return new QBDouble( obj1.doubleValue() * obj2.doubleValue() );
		}
	};
	
	public static final QBFunction DIV = new QBFunction(2) {	
		/**
		 * 
		 */
		private static final long serialVersionUID = 6929787162778332248L;

		@Override
		public QBObject execute(QBObject[] args) {
			QBPrimitive obj2 = (QBPrimitive) args[0];
			QBPrimitive obj1 = (QBPrimitive) args[1];
			
			return new QBDouble( obj1.doubleValue() / obj2.doubleValue() );
		}
	};
	
	public static final QBFunction DIVI = new QBFunction(2) {	
		/**
		 * 
		 */
		private static final long serialVersionUID = 6929787162778332248L;

		@Override
		public QBObject execute(QBObject[] args) {
			QBPrimitive obj2 = (QBPrimitive) args[0];
			QBPrimitive obj1 = (QBPrimitive) args[1];
			
			if(obj2 instanceof QBDouble)
				return new QBDouble( obj1.doubleValue() / obj2.doubleValue() );
			
			return new QBInt( obj1.intValue() / obj2.intValue() );
		}
	};
	
	public static final QBFunction ADDI = new QBFunction(2) {	
		/**
		 * 
		 */
		private static final long serialVersionUID = -458278389954945664L;

		@Override
		public QBObject execute(QBObject[] args) {
			QBPrimitive obj2 = (QBPrimitive) args[0];
			QBPrimitive obj1 = (QBPrimitive) args[1];
			
			if(obj2 instanceof QBDouble)
				return new QBDouble( obj1.doubleValue() + obj2.doubleValue() );
			
			return new QBInt( obj1.intValue() + obj2.intValue() );
		}
	};
	
	public static final QBFunction SUBI = new QBFunction(2) {	
		/**
		 * 
		 */
		private static final long serialVersionUID = 8875619169775793806L;

		@Override
		public QBObject execute(QBObject[] args) {
			QBPrimitive obj2 = (QBPrimitive) args[0];
			QBPrimitive obj1 = (QBPrimitive) args[1];
			
			if(obj2 instanceof QBDouble)
				return new QBDouble( obj1.doubleValue() - obj2.doubleValue() );
			
			return new QBInt( obj1.intValue() - obj2.intValue() );
		}
	};
	
	public static final QBFunction MULI = new QBFunction(2) {	
		/**
		 * 
		 */
		private static final long serialVersionUID = -5397892833871103232L;

		@Override
		public QBObject execute(QBObject[] args) {
			QBPrimitive obj2 = (QBPrimitive) args[0];
			QBPrimitive obj1 = (QBPrimitive) args[1];
			
			if(obj2 instanceof QBDouble)
				return new QBDouble( obj1.doubleValue() * obj2.doubleValue() );
			
			return new QBInt( obj1.intValue() * obj2.intValue() );
		}
	};
	
	public static final QBFunction POWI = new QBFunction(2) {	
		/**
		 * 
		 */
		private static final long serialVersionUID = 5551940214654416862L;

		@Override
		public QBObject execute(QBObject[] args) {
			QBPrimitive obj2 = (QBPrimitive) args[0];
			QBPrimitive obj1 = (QBPrimitive) args[1];
			
			if(obj2 instanceof QBDouble)
				return new QBDouble( Math.pow(obj1.doubleValue(), obj2.doubleValue()) );
			
			return new QBInt( (int) Math.pow(obj1.intValue(), obj2.intValue()) );
		}
	};

	
	public static final QBFunction POW = new QBFunction(2) {	
		/**
		 * 
		 */
		private static final long serialVersionUID = 5551940214654416862L;

		@Override
		public QBObject execute(QBObject[] args) {
			QBPrimitive obj2 = (QBPrimitive) args[0];
			QBPrimitive obj1 = (QBPrimitive) args[1];
			
			return new QBDouble( Math.pow(obj1.doubleValue(), obj2.doubleValue()) );
		}
	};
	
	public static final QBFunction AND = new QBFunction(2) {	
		/**
		 * 
		 */
		private static final long serialVersionUID = -2549738198868255228L;

		@Override
		public QBObject execute(QBObject[] args) {
			QBInt obj2 = (QBInt) args[0];
			QBInt obj1 = (QBInt) args[1];
			
			return new QBInt( obj1.intValue() & obj2.intValue() );
		}
	};
	
	public static final QBFunction OR = new QBFunction(2) {	
		/**
		 * 
		 */
		private static final long serialVersionUID = -4783153883428278962L;

		@Override
		public QBObject execute(QBObject[] args) {
			QBInt obj2 = (QBInt) args[0];
			QBInt obj1 = (QBInt) args[1];
			
			return new QBDouble( obj1.intValue() | obj2.intValue() );
		}
	};
	
	public static final QBFunction MOD = new QBFunction(2) {	
		/**
		 * 
		 */
		private static final long serialVersionUID = -4919278872817004237L;

		@Override
		public QBObject execute(QBObject[] args) {
			QBPrimitive obj2 = (QBPrimitive) args[0];
			QBPrimitive obj1 = (QBPrimitive) args[1];
			
			return new QBDouble( obj1.doubleValue() % obj2.doubleValue() );
		}
	};
	
	public static final QBFunction MODI = new QBFunction(2) {	
		/**
		 * 
		 */
		private static final long serialVersionUID = -4919278872817004237L;

		@Override
		public QBObject execute(QBObject[] args) {
			QBPrimitive obj2 = (QBPrimitive) args[0];
			QBPrimitive obj1 = (QBPrimitive) args[1];
			
			if(obj2 instanceof QBDouble)
				return new QBDouble( obj1.doubleValue() % obj2.doubleValue() );
			
			return new QBInt( obj1.intValue() % obj2.intValue() );
		}
	};
	
	public static final QBFunction XOR = new QBFunction(2) {	
		/**
		 * 
		 */
		private static final long serialVersionUID = -1376100825702322135L;

		@Override
		public QBObject execute(QBObject[] args) {
			QBInt obj2 = (QBInt) args[0];
			QBInt obj1 = (QBInt) args[1];
			
			return new QBInt( obj1.intValue() ^ obj2.intValue() );
		}
	};
	
	public static final QBFunction NOT = new QBFunction(1) {	
		/**
		 * 
		 */
		private static final long serialVersionUID = -2508035483288699515L;

		@Override
		public QBObject execute(QBObject[] args) {
			QBInt obj2 = (QBInt) args[0];
			
			return new QBInt( ~obj2.intValue() );
		}
	};
	
	public static final QBFunction GT = new QBFunction(2) {	
		/**
		 * 
		 */
		private static final long serialVersionUID = -3405854489760677489L;

		@Override
		public QBObject execute(QBObject[] args) {
			QBPrimitive obj2 = (QBPrimitive) args[0];
			QBPrimitive obj1 = (QBPrimitive) args[1];
			
			return new QBInt( obj1.compareTo(obj2) == 1 ? 1 : 0 );
		}
	};
	
	public static final QBFunction LT = new QBFunction(2) {	
		/**
		 * 
		 */
		private static final long serialVersionUID = -3758428556267202922L;

		@Override
		public QBObject execute(QBObject[] args) {
			QBPrimitive obj2 = (QBPrimitive) args[0];
			QBPrimitive obj1 = (QBPrimitive) args[1];
			
			return new QBInt( obj1.compareTo(obj2) == -1 ? 1 : 0 );
		}
	};
	
	public static final QBFunction EQ = new QBFunction(2) {	
		/**
		 * 
		 */
		private static final long serialVersionUID = -3756644511236409624L;

		@Override
		public QBObject execute(QBObject[] args) {
			QBPrimitive obj2 = (QBPrimitive) args[0];
			QBPrimitive obj1 = (QBPrimitive) args[1];
			
			return new QBInt( obj1.compareTo(obj2) == 0 ? 1 : 0 );
		}
	};
	
	public static final QBFunction LTE = new QBFunction(2) {	
		/**
		 * 
		 */
		private static final long serialVersionUID = -8301307408804643494L;

		@Override
		public QBObject execute(QBObject[] args) {
			QBPrimitive obj2 = (QBPrimitive) args[0];
			QBPrimitive obj1 = (QBPrimitive) args[1];
			
			return new QBInt( obj1.compareTo(obj2) <= 0 ? 1 : 0 );
		}
	};
	
	public static final QBFunction GTE = new QBFunction(2) {	
		/**
		 * 
		 */
		private static final long serialVersionUID = -3805394859773545337L;

		@Override
		public QBObject execute(QBObject[] args) {
			QBPrimitive obj2 = (QBPrimitive) args[0];
			QBPrimitive obj1 = (QBPrimitive) args[1];
			
			return new QBInt( obj1.compareTo(obj2) >= 0 ? 1 : 0 );
		}
	};
	
	public static final QBFunction NE = new QBFunction(2) {	
		/**
		 * 
		 */
		private static final long serialVersionUID = -5958431913044160689L;

		@Override
		public QBObject execute(QBObject[] args) {
			QBPrimitive obj2 = (QBPrimitive) args[0];
			QBPrimitive obj1 = (QBPrimitive) args[1];
			
			return new QBInt( obj1.compareTo(obj2) != 0 ? 1 : 0 );
		}
	};
	
	public static final QBFunction AS = new QBFunction(2){

		@Override
		public QBObject execute(QBObject[] args) {
			QBStruct obj2 = (QBStruct) args[0];
			QBPrimitive obj1 = (QBPrimitive) args[1];
			
			if( obj2 == QBInt.struct )
				return new QBInt( obj1.intValue() );
			
			else if(obj2 == QBDouble.struct )
				return new QBDouble( obj1.doubleValue() );
			
			else if(obj2 == QBChar.struct)
				return new QBChar( obj1.charValue() );
			
			throw new RuntimeException("Exhauseted all options");
		}	
	};
}
