package com.modulus.qbar.integration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.modulus.qbar.core.QBFunction;
import com.modulus.qbar.core.QBObject;

public class QBWrappedMethod extends QBFunction{
	private transient Method wrapped;
	private boolean isStatic;
	
	private String methodName;
	private Class<?> clazz;
	
	public QBWrappedMethod(Method toWrap) {
		super(toWrap.getParameterTypes().length);
		this.wrapped = toWrap;
		
		isStatic = Modifier.isStatic(toWrap.getModifiers());
		
		if(!isStatic){
			this.setArgc(this.getArgc() + 1);
		}
	}
	
	public QBWrappedMethod( Class<?> clazz, String methodName, int argc ){
		super( argc );
		
		this.clazz = clazz;
		this.methodName = methodName;
	}
	
	// since java is sometimes annoying and will not let you serialize Methods,
	// we need to send the information and generate them on the fly
	private void setup(){
		Method[] methods = this.clazz.getMethods();
		
		for(Method method : methods ){
			if(method.getName().equals(methodName)){
				this.wrapped = method;
				break;
			}
		}
		
		isStatic = Modifier.isStatic(this.wrapped.getModifiers());
	}
	
	@Override
	public QBObject execute(QBObject[] args){
		try {
			if(wrapped == null)
				setup();
			
			if(isStatic){
				return executeStatic(args);
			} else{
				Object[] objs = new Object[args.length - 1];
				Object caller =args[args.length - 1].getWrapped();
				
				for( int i = 0;i < args.length-1; i++){
		//			System.out.println("args["+i+"]: " + args[i] + " wrap: " + args[i].getWrapped());
					objs[i] = args[i].getWrapped();
				}
				
		//		System.out.println("Method " + wrapped + "\nargs: " + Arrays.toString(objs) + "\nargs2: " + Arrays.toString(args));
				return QBWrappedObject.wrap(wrapped.invoke(caller, objs));
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	private QBObject executeStatic(QBObject[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Object[] objs = new Object[args.length];
		for( int i = 0;i < objs.length; i++){
			objs[i] = args[i].getWrapped();
		}
	//	System.out.println(Arrays.toString(objs) + " " + this.getArgc() + "\n\t" + wrapped);
		
		return QBWrappedObject.wrap(wrapped.invoke(null, objs));
	}

}
