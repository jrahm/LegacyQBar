package com.modulus.qbar.core.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;

import com.modulus.common.collections.ArrayStack;
import com.modulus.common.collections.Stack;
import com.modulus.qbar.core.QBNamespace;
import com.modulus.qbar.core.QBObject;
import com.modulus.qbar.core.QBStruct;
import com.modulus.qbar.core.QBUtilityNamespace;
import com.modulus.qbar.core.primitive.QBPrimitive;

public class Core implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2963257754134148521L;
	private QBUtilityNamespace globalNamespace;
	private Stack<QBObject> stack;
	
	public Core(){
		this.globalNamespace = new QBUtilityNamespace( null ){
			private static final long serialVersionUID = 3278462104573891204L;
			private String name = "Global";
			@Override
			public String getName() {
				return name;
			}

			@Override
			public void construct() {
			}
			
		};
		
		this.stack = new ArrayStack<QBObject>();
	}
	
	public QBNamespace getGlobalNamespace(){
		return globalNamespace;
	}
	
	public Stack<QBObject> getStack(){
		return stack;
	}
	
	public void merge(Core other){
		this.globalNamespace.incoporate( other.getGlobalNamespace() );
	}
	
	public static boolean QBObjectIsTrue(QBObject obj){
		return obj instanceof QBPrimitive && ((QBPrimitive) obj).intValue() != 0;
	}

	public void writeSelf(OutputStream out) throws IOException {
		ObjectOutputStream oout = new ObjectOutputStream(out);
		
		
		oout.writeObject( globalNamespace );
		oout.flush();
		oout.close();
	}

	public void readSelf(InputStream in) throws IOException, ClassNotFoundException {
		ObjectInputStream oin = new ObjectInputStream(in);
		Object ret = oin.readObject();
		
		oin.close();
		
		this.globalNamespace = (QBUtilityNamespace) ret;
	}
}
