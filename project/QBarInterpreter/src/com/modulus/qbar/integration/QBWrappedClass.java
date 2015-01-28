package com.modulus.qbar.integration;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import com.modulus.qbar.core.QBFunction;
import com.modulus.qbar.core.QBNamespace;
import com.modulus.qbar.core.QBObject;
import com.modulus.qbar.core.QBStruct;

public class QBWrappedClass extends QBStruct{
	private static final long serialVersionUID = -4889124451749124731L;
	private static final Map<Class<?>, QBWrappedClass> wrappedClasses = new HashMap<Class<?>, QBWrappedClass>();
	
	public static QBStruct wrapClass(Class<?> clazz, QBNamespace nmspce){
		QBWrappedClass ret = wrappedClasses.get(clazz);
		
		
		if(ret == null){
			ret = new QBWrappedClass(clazz, nmspce);
			wrappedClasses.put(clazz, ret);
		}
		
		return ret;
	}
	
	private QBWrappedClass(Class<?> toWrap, QBNamespace nmspce) {
		super("");
		String name = toWrap.getName();
		this.setName(name.substring(name.lastIndexOf('.') + 1));
		
		Method[] methods = toWrap.getMethods();
		
		for( Method m : methods){
			int mod = m.getModifiers();
			int argc = m.getParameterTypes().length;
			if(!Modifier.isStatic(mod)){
				argc += 1;
			}
			
			QBFunction func = new QBWrappedMethod(toWrap, m.getName(), argc);
			
			
			if(Modifier.isStatic(mod)){
				String methodName = m.getName();
				
				if(methodName.startsWith("new$0x20")){
					methodName = methodName.substring(8);
					methodName = "new$0x20" + methodName;
				}
				
				nmspce.set(methodName, func);
			} else {
				this.set(m.getName(), func);
			}
		}
		
		Constructor<?>[] constructors = toWrap.getConstructors();
		if(constructors.length > 0){
			QBWrappedConstructor toAdd = new QBWrappedConstructor(constructors[0].getParameterTypes().length, toWrap);
			nmspce.set( "new$0x20" + this.getName(), toAdd);
		}
	}

}
