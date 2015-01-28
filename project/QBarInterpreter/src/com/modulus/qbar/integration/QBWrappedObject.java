package com.modulus.qbar.integration;

import java.util.Arrays;
import java.util.List;

import com.modulus.common.collections.MArrays;
import com.modulus.qbar.core.QBObject;
import com.modulus.qbar.core.interpreter.QBInterpreter;
import com.modulus.qbar.core.primitive.QBChar;
import com.modulus.qbar.core.primitive.QBDouble;
import com.modulus.qbar.core.primitive.QBInt;
import com.modulus.qbar.lang.QBArrayList;
import com.modulus.qbar.lang.QBList;
import com.modulus.qbar.lang.QBMaybe;
public class QBWrappedObject extends QBObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 400259956418357800L;
	private Object wrapped;
	
	private QBWrappedObject(Object wrapped) {
		super( QBWrappedClass.wrapClass(wrapped.getClass(), QBInterpreter.instance().getGlobalNamespace()) );
		this.wrapped = wrapped;
	}
	
	public Object getWrapped(){
		return wrapped;
	}

	public static QBObject wrap(Object invoke) {
		if(invoke == null){
			return new QBMaybe.Maybe(null);
		}
		
		if(invoke instanceof QBObject) {
			return (QBObject) invoke;
		}
		
		if ( invoke instanceof Object[] ){
			return fromList(
					Arrays.asList((Object[]) invoke)
			);
		}
		
		if ( invoke instanceof byte[] ){
			List<Byte> lst = MArrays.asList((byte[]) invoke);
			return fromList(
					lst
			);
		}
		
		if ( invoke instanceof int[] ){
			return fromList(
					MArrays.asList((int[]) invoke)
			);
		}
		
		if ( invoke instanceof short[] ){
			return fromList(
					MArrays.asList((short[]) invoke)
			);
		}
		
		if ( invoke instanceof long[] ){
			return fromList(
					MArrays.asList((long[]) invoke)
			);
		}
		
		if ( invoke instanceof boolean[] ){
			return fromList(
					MArrays.asList((boolean[]) invoke)
			);
		}
		
		if ( invoke instanceof char[] ){
			return fromList(
					MArrays.asList((char[]) invoke)
			);
		}
		
		if ( invoke instanceof double[] ){
			return fromList(
					MArrays.asList((double[]) invoke)
			);
		}
		
		if ( invoke instanceof float[] ){
			return fromList(
					MArrays.asList((float[]) invoke)
			);
		}
		
		if( invoke instanceof List<?> ){
			return fromList( (List<?>) invoke );
		}
		
		if(invoke instanceof Double || invoke instanceof Float){
			return new QBDouble(((Number) invoke).doubleValue());
		}
		
		if(invoke instanceof Number){
			return new QBInt(((Number) invoke).intValue());
		}
		
		if(invoke instanceof Boolean){
			return new QBInt(((Boolean) invoke) ? 1 : 0);
		}
		
		if(invoke instanceof Character){
			return new QBChar((Character)invoke);
		}
		
		return new QBWrappedObject(invoke);
	}
	
	public static long timeSpent = 0;
	private static QBObject fromList(final List<?> invoke) {
		QBList tmp = new QBList() {
			
			@Override
			public double size() {
				return invoke.size();
			}
			
			@Override
			public void remove(QBObject obj) {
				invoke.remove(obj);
			}
			
			@Override
			public QBList realSubList(int off, int len) {
				return (QBList) QBWrappedObject.wrap( invoke.subList(off, off+len) );
			}
			
			@Override
			public void insert(int idx, QBObject obj) {
			}
			
			@Override
			public QBObject get(int idx) {
				return QBWrappedObject.wrap( invoke.get(idx) );
			}
			
			@Override
			public QBObject add(QBObject obj) {
				// TODO Auto-generated method stub
				return null;
			}
		};
	//	timeSpent += (t2 - t1);
		return tmp;
	}

	public String toString(){
		return wrapped.toString();
	}

}
