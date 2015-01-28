package com.modulus.qbar.core;

import java.util.ArrayList;
import java.util.List;

import com.modulus.qbar.core.interpreter.QBInterpreter;
import com.modulus.qbar.integration.QBWrappedObject;


public class QBStruct extends QBObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1271467629591493953L;
	
	private List<QBStruct> superStruct = new ArrayList<QBStruct>();
	

	
	public static final QBStruct clazz = new QBStruct(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1915609768305262326L;

		@Override
		public QBObject newInstance() {
			return this;
		}
	};
	public static final QBStruct array = new QBStruct("Array"){
		/**
		 * 
		 */
		private static final long serialVersionUID = -6966880297599366521L;

		@Override
		public QBObject newInstance() {
			return this;
		}
	};
	
	static{
		clazz.name = "Class";
		clazz.pack = "core";
		
		clazz.setStruct(clazz);
		clazz.setSuper( QBInterpreter.instance().getGlobalNamespace() );
	}
	
	private String name;
	private String pack;
	
	private QBStruct(){ super(null); init(); };
	
	protected QBStruct(boolean nill) { super(clazz); init(); };
	public QBStruct(String name){
		super(clazz);
		this.name = name;
		
		QBInterpreter.instance().getGlobalNamespace().set(name, this);
		init();
	}
	
	public QBStruct(String name, QBStruct sup){
		super(clazz);
		this.name = name;
		
		this.load(sup);
		this.superStruct.add(sup);
		
		QBInterpreter.instance().getGlobalNamespace().set(name, this);
		init();
	}
	
	public String getName(){
		return name;
	}
	
	public String getPackage(){
		return pack;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public QBObject newInstance(){
		return null;
	}
	
	public String toString(){
		return name;
	}
	
	public void addSuperStruct( QBStruct sup ){
		this.superStruct.add(sup);
		this.load(sup);
	}
	
	private void init(){
		this.set("getType", new QBFunction(1) {
			private static final long serialVersionUID = -278677571392736723L;

			@Override
			public QBObject execute(QBObject[] args) {
				return this;
			}
		});
		
		this.set("isa", new QBFunction(2){
			private static final long serialVersionUID = 3535644877614653590L;

			@Override
			public QBObject execute(QBObject[] args) {
				QBObject ths = args[1];
				QBStruct struct = (QBStruct) args[0];
				
				return QBWrappedObject.wrap( ths.getStruct().instanceOf(struct) );
			}
			
		});
		
		
	}
	
	public boolean instanceOf(QBStruct struct) {
		if( this == struct )
			return true;
		
		for( QBStruct sup : superStruct )
			if( sup.instanceOf(struct) )
				return true;
		
		return false;
	}
}
