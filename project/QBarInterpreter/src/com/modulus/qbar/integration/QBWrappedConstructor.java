package com.modulus.qbar.integration;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.modulus.qbar.core.QBFunction;
import com.modulus.qbar.core.QBObject;

public class QBWrappedConstructor extends QBFunction{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3025785234264500875L;
	private transient Constructor<?> wrapped;
	
	private Class<?> clazz;
	
	public QBWrappedConstructor(int argc, Constructor<?> wrapped) {
		super(argc);
		this.wrapped = wrapped;
	}
	
	public QBWrappedConstructor(int argc, Class<?> clazz){
		super(argc);
		this.clazz = clazz;
	}
	
	private void setup(){
		Constructor<?>[] constructors = this.clazz.getConstructors();
		this.wrapped = constructors[0];
	}

	@Override
	public QBObject execute(QBObject[] args) {
		try{
			if(wrapped == null)
				setup();
			
			Object[] objs = new Object[args.length];
			
			for( int i = 0;i < objs.length; i ++){
				objs[i] = args[i].getWrapped();
			}
			
			Object ret = wrapped.newInstance( objs );
			return QBWrappedObject.wrap(ret);
		} catch( Exception e ){
			throw new RuntimeException("Failed to Create new Instance of " + wrapped.getClass());
		}
	}

}
