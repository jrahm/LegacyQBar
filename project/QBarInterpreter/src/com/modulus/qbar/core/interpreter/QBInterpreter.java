package com.modulus.qbar.core.interpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.modulus.common.collections.Stack;
import com.modulus.qbar.core.QBFunction;
import com.modulus.qbar.core.QBNamespace;
import com.modulus.qbar.core.QBObject;
import com.modulus.qbar.core.QBStruct;
import com.modulus.qbar.core.QBUtilityNamespace;
import com.modulus.qbar.core.parser.Core;
import com.modulus.qbar.core.parser.QBParser;
import com.modulus.qbar.core.primitive.QBChar;
import com.modulus.qbar.core.primitive.QBDouble;
import com.modulus.qbar.core.primitive.QBInt;
import com.modulus.qbar.core.primitive.QBPrimitive;

public class QBInterpreter implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3717481804592061086L;
	private static QBInterpreter instance;
	private Core core;
	private transient QBParser parser;
	private QBJarLoader loader;

	private static class QBThread extends Thread{
		private QBInterpreter instance;
		
		public QBThread( QBInterpreter cur ){
			this.instance = cur;
		}
		
		public QBInterpreter instance(){
			return instance;
		}
	}
	
	public QBThread run(){
		
		QBThread thread = new QBThread(this){
			public void run(){
				QBFunction main = (QBFunction) core.getGlobalNamespace().get("main");
				main.execute(new QBObject[]{});
			}
		};
		
		
		thread.start();
		return thread;
	}
	
	public static QBInterpreter instance(){
		Thread t = Thread.currentThread();
		
		if( t instanceof QBThread )
			return ((QBThread) t).instance();
		
		return instance;
	}
	
	public QBInterpreter( String code ){
		try {
			loader = new QBJarLoader("/usr/lib/qbar/lib/endorsed");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		parser = new QBParser( this );
		
		instance = this;
		
		this.core = parser.getSystemCore();
		loadLang();
		
		parser.parseCode(code);
		
		
		parser.resetCore();
		
		this.getGlobalNamespace().set("Null", new QBObject(null));
		((QBUtilityNamespace) this.getGlobalNamespace()).setStruct( new QBStruct("NamespaceUtility") );
		

	}
	
	protected QBInterpreter(){
		try {
			loader = new QBJarLoader("/usr/lib/qbar/lib/endorsed");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		instance = this;
		this.core = new Core();
		
		loadLang();
		this.getGlobalNamespace().set("Null", new QBObject(null));
		((QBUtilityNamespace) this.getGlobalNamespace()).setStruct( new QBStruct("NamespaceUtility") );
	}
	
	public QBNamespace getGlobalNamespace(){
		return core.getGlobalNamespace();
	}
	
	public Stack<QBObject> getStack(){
		return core.getStack();
	}
	
	public void merge(QBInterpreter other){
		this.core.merge(other.core);
	}
	public void writeSelf( OutputStream out ) throws IOException{
		this.core.writeSelf( out );
	}
	
	public static QBInterpreter readInterpreter( InputStream in ) throws IOException, ClassNotFoundException{
		QBInterpreter ret = new QBInterpreter();
		
		ret.parser = new QBParser( ret );
		ret.core = new Core();
		
		ret.core.readSelf( in );
		
		return ret;
	}
	
	public void writeSelfCompressed(final OutputStream out) throws IOException{
		GZIPOutputStream zipOut = new GZIPOutputStream(out);
		writeSelf(zipOut);
	}
	
	public static QBInterpreter readInterpreterFromSource( InputStream in ) throws IOException{
		BufferedReader reader = new BufferedReader( new InputStreamReader( in ) );
		StringBuffer buf = new StringBuffer();
		
		String line;
		boolean begin = true;
		while((line = reader.readLine()) != null){
			if(begin && line.startsWith("#"))
				continue;
			
			buf.append(line + "\n");
			
			if(begin && line.trim() != "")
				begin = false;
		}
		
		QBInterpreter old = instance();
		QBInterpreter interpreter = new QBInterpreter(buf.toString());
		
		if(old != null)
			instance = old;
		
		return interpreter;
	}
	
	public static QBInterpreter readInterpreterCompressed( InputStream in ) throws IOException, ClassNotFoundException{
		GZIPInputStream zipIn = new GZIPInputStream(in);
		return readInterpreter(zipIn);
	}
	
	private void loadLang(){
		QBNamespace global = core.getGlobalNamespace();
		
		global.set(QBInt.struct.getName(), QBInt.struct);
		global.set(QBDouble.struct.getName(), QBDouble.struct);
		global.set(QBChar.struct.getName(), QBChar.struct);
		global.set(QBPrimitive.struct.getName(), QBPrimitive.struct);
		
	}

	public QBParser getParser() {
		return parser;
	}
	
	public ClassLoader getClassLoader() {
		return loader.getLoader();
	}
	
}

