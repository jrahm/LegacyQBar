package com.modulus.qbar.lang;

import com.modulus.qbar.core.QBFunction;
import com.modulus.qbar.core.QBObject;
import com.modulus.qbar.core.QBStruct;
import com.modulus.qbar.core.exceptions.ExecutionException;
import com.modulus.qbar.integration.QBWrappedObject;
public class QBMaybe {
	public static class Maybe extends QBObject{
		
		public static final QBStruct struct = new QBStruct("Maybe");

		static{
			struct.set("getValue", new QBFunction(1){
				@Override
				public QBObject execute(QBObject[] args) {
					Maybe ths = (Maybe)args[args.length-1];
					return QBWrappedObject.wrap(ths.getValue());
				}
			});

			struct.set("isNull", new QBFunction(1){
				@Override
				public QBObject execute(QBObject[] args) {
					Maybe ths = (Maybe)args[args.length-1];
					return QBWrappedObject.wrap(ths.isNull());
				}
			});
		}
		
		public Maybe(QBObject value) {
			super(struct);
			this.value = value;
		}

		private QBObject value;

		
		public QBObject getValue(){
			if(isNull()){
				throw new ExecutionException("Attempted te get value of Null Type Maybe!");
			}
			
			return value;
		}
		
		public boolean isNull(){
			return value == null;
		}
	}
	
	public static QBObject new$0x20Maybe(QBObject obj){
		return new Maybe(obj);
	}
	
	public static QBObject new$0x20Null(){
		return new Maybe(null);
	}
}
