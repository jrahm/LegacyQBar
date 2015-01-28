package com.modulus.qbar.lang;

import com.modulus.qbar.core.QBFunction;
import com.modulus.qbar.core.QBObject;
import com.modulus.qbar.core.QBStruct;
import com.modulus.qbar.core.parser.QBExpressionParser;
import com.modulus.qbar.core.primitive.QBDouble;
import com.modulus.qbar.core.primitive.QBInt;

public abstract class QBList extends QBObject implements Cloneable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3820130303027533210L;

	static final public QBStruct list = new QBStruct("list");
	
	static final public QBFunction ADD = new QBFunction(2) {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1966242605460532062L;

		@Override
		public QBObject execute(QBObject[] args) {
			QBList ths = (QBList)args[1];
			ths.add(args[0]);
			return ths;
		}
	};
	
	static final public QBFunction INSERT = new QBFunction(3) {
		/**
		 * 
		 */
		private static final long serialVersionUID = -4254176176696884954L;

		@Override
		public QBObject execute(QBObject[] args) {
			QBList ths = (QBList)args[2];
			ths.insert( ((QBInt)args[1]).intValue(), args[0] );
			return ths;
		}
	};
	
	static final public QBFunction REMOVE = new QBFunction(2) {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -5952990904494730121L;

		@Override
		public QBObject execute(QBObject[] args) {
			QBList ths = (QBList)args[1];
			ths.remove( args[0] );
			return ths;
		}
	};
	
	static final public QBFunction GET = new QBFunction(2) {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -4826771802410960653L;

		@Override
		public QBObject execute(QBObject[] args) {
			QBList ths = (QBList)args[1];
			return ths.get(((QBInt) args[0]).intValue());
		}
	};
	
	static final public QBFunction ITERATOR = new QBFunction(2) {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 3915900913102253534L;

		@Override
		public QBObject execute(QBObject[] args) {
			QBIterator<QBList> ret = new QBListIterator( (QBList)args[0] );
			return ret;
		}
	};
	
	static final public QBFunction SUBLIST = new QBFunction(3) {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 3915900913102253534L;

		@Override
		public QBObject execute(QBObject[] args) {
			QBList ths = (QBList) args[2];
			int off = ((QBInt) args[0]).intValue();
			int len = ((QBInt) args[1]).intValue();
			
			return new QBSubList(ths, off, len);
		}
	};

	
	public static void init(){
		list.set("add", ADD);
		list.set("remove", REMOVE);
		list.set("insert", INSERT);
		list.set("get", GET);
		
		list.set("#", GET);
		list.set("+<", ADD);
		list.set("-<", REMOVE);
		
		list.set("iterator", ITERATOR);
		list.set("subList", SUBLIST);
		
		QBExpressionParser.mapOp("#", 9);
		QBExpressionParser.mapOp("+<", 1024);
		QBExpressionParser.mapOp("-<", 1024);
	}
	
	public QBList() {
		super(list);
	}
	
	public QBList(QBStruct struct) {
		super(struct);
	}
	
	@Override
	public QBObject get(String name){
		if(name.equals("length")){
			double size = size();
			
			if( Double.isInfinite(size) || Double.isNaN(size) || size %1 != 0)
				return new QBDouble(size());
			
			return new QBInt((int)size());
		}
		
		return super.get(name);
	}
	
	abstract public QBList realSubList( int off, int len );
	
	abstract public QBObject add(QBObject obj);
	
	abstract public void remove(QBObject obj);
	
	abstract public QBObject get(int idx);
	
	abstract public double size();
	
	abstract public void insert(int idx, QBObject obj);
	
//	abstract public QBList concat( QBList other );
}
