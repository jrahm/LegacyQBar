package com.modulus.qbar.core.interpreter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.modulus.dataread.expressions.Statement;
import com.modulus.qbar.core.QBFile;
import com.modulus.qbar.core.QBFunction;
import com.modulus.qbar.core.QBObject;
import com.modulus.qbar.core.parser.AnnotationReader;
import com.modulus.qbar.core.parser.QBExpressionParser;
import com.modulus.qbar.core.parser.QBFileType;
import com.modulus.qbar.core.parser.QBParser;
import com.modulus.qbar.integration.QBWrappedObject;

public class Main {
	private static String[] clsPath;
	private static boolean compile;
	private static boolean zip;
	private static boolean debug;
	private static boolean interactive;
	private static String file;
	private static String[] progArgs;
	public static void main(String[] args){
		try{
			if(args.length == 0){
				printHelp();
				return;
			}
			
			parseArgs(args);
			
			File f = file == null ? null : QBFile.getQBFile( file );
			
			if(f != null && f.isDirectory()) {
				System.out.println("Cannot execute on directory!");
				printHelp();
				
				return;
			}
			
			QBParser.addAnnotationReader( new AnnotationReader() {
				
				@Override
				public void readAnnotation(Statement stmt) {
					String header = stmt.getHeader().substring(1);
					if(header.startsWith("infix")){
						header = header.substring(5).trim();
						
						String[] headers = header.split("\\s+");
						int prec = 30;
						String op;
						
						if(headers.length == 2){
							prec = Integer.parseInt(headers[1]);
						}
						
						op = headers[0];
						QBExpressionParser.mapOp(op, prec);
					}
				}
			});
			
			// System.out.println("Hello?");
			
			if( compile ){
				compile(f, zip);
			} else if(interactive) {
				QBInteractiveInterpreter interpreter = new QBInteractiveInterpreter();
				interpreter.startInterpreter();
			} else{
				go(f);
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void go( File f ) throws Exception{
		QBInterpreter interpreter = openFile(f);
		
		long time = 0, time2 = 0;
		if(debug){
			System.out.println("Namespace: " + interpreter.getGlobalNamespace().toString2());
			time = System.currentTimeMillis();
		}
		
		QBFunction func = (QBFunction) interpreter.getGlobalNamespace().get("main");
		QBObject finish = func.execute(new QBObject[]{});
		
		if(debug){
			time2 = System.currentTimeMillis();	
			System.out.println("Result: " + finish);		
			System.out.println("Total time: " + (time2 - time) / 1000.0 + " seconds");
			
			System.out.println("Time spent on one method: " + QBObject.totalTime / 1000.0 + " seconds");
		}
	}
	
	public static void compile( File f, boolean zip ) throws Exception{
		QBInterpreter interpreter = openFile(f);
		
		String str = f.getName();
		int idx = str.lastIndexOf('.');
		if (idx >= 0) {
			str = str.substring(0,idx);
		}
		
		if(zip) {
			str += ".qbz";
		} else {
			str += ".qbc";
		}
		
		File newFile = new File(str);
		OutputStream out = new FileOutputStream(newFile);
		
		if( zip ) {
			interpreter.writeSelfCompressed(out);
		} else {
			interpreter.writeSelf(out);
		}
	}
	
	private static void parseArgs(String[] args) {
		List<String> path = new ArrayList<String>();

		path.add(".");
		
		for( int i = 0; i < args.length; i++) {
			String str = args[i];
			if(str.startsWith("-")) {
				if(str.equals("-p") || str.equals("--path")) {
					if( i == args.length ) {
						System.err.println("--path takes 1 argument");
						System.exit(1);
					}
					
					String pathArg = args[ ++i ];
					String[] paths = pathArg.split(";");
					path.addAll( Arrays.asList(paths) );
				} else if( str.equals("-c") || str.equals("--compile") ){
					if(compile)
						warn("compiling already enabled");
					
					compile = true;
				} else if( str.equals("-z") || str.equals("--zip") ){
					if(compile)
						warn("compiling already enabled");
					if(zip)
						warn("zipping already enabled");
					
					compile = true;
					zip = true;
				} else if( str.equals("--debug") ){
					debug = true;
				} else if( str.equals("-i") ) {
					interactive = true;
				} else {
					System.err.println("Illegal option " + str);
				}
			} else {
				progArgs = new String[ args.length - i ];
				System.arraycopy(args, i, progArgs, 0, progArgs.length);
				file = progArgs[0];
				break;
			}
		}
		
		clsPath = path.toArray(new String[path.size()]);
	}

	private static void printHelp() {
		System.out.println("QBarInterpreter v. 0.028 BETA (More Like DELTA)\nUsage: qbar [ -p [CLASSPATH] ] [FILENAME]\nExecutes a qbar " +
				"file using the classpath defined as the search path for modules\n\nInterpreter executes the `main` function of" +
				" the file.\nOptions:\n\n\t-p : add a path to the classpath\n\t-c : compile source file\n\t-z : compile source file and zip it\n\t--debug : enable debugging mode (for development)" + "\n\nAuthor: Josh Rahm");
	}
	
	public static void warn(String str){
		System.out.println("[Warn] " + str);
	}

	public static QBInterpreter openFile( File f ) throws Exception{
		QBFileType type = QBFileType.getFileType( f.getName() );
		return type.getReader().readFromStream( new FileInputStream(f) );
	}

	public static String[] getPath() {
		return clsPath;
	}
	
	public static void debug(Class<?> cls, String str){
		if( debug ){
			System.out.println("[DEBUG] - " + cls.getSimpleName() + " - " + str);
		}
	}
	
	public static void prettyThrow( RuntimeException e ){
		if( debug ) {
			throw e;
		}
		
		List<String> lines = new ArrayList<String>();
		List<String> messages = new ArrayList<String>();
		
		Exception cur = e;
		while( cur != null ) {
			messages.add("cur");
		}
	}
}
