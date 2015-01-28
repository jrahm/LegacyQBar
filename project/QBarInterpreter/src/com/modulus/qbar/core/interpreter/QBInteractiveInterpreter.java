package com.modulus.qbar.core.interpreter;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

import com.modulus.dataread.expressions.Statement;
import com.modulus.qbar.core.QBObject;
import com.modulus.qbar.core.QBSyntheticFunction;
import com.modulus.qbar.core.parser.ByteOperation;
import com.modulus.qbar.core.parser.QBParser;
import com.modulus.qbar.core.parser.stmt.QBStatement;
import com.modulus.qbar.lang.QBMaybe;

public class QBInteractiveInterpreter extends QBInterpreter{
	private BufferedReader console;
	private QBParser parser;
	private QBStatement alwaysSuper = new QBStatement( QBParser.formatter );
	public static void main(String[] args) {
		QBInteractiveInterpreter interpreter = new QBInteractiveInterpreter();
		interpreter.startInterpreter();
	}
	
	public QBInteractiveInterpreter(){
		super();
		
		alwaysSuper.setHeader("let tmp = do");
		
		console = new BufferedReader( new InputStreamReader( System.in ) );
		this.parser = new QBParser( this );
	}
	
	public void startInterpreter(){
		while(true){
			try{
				TransparentFunction func = readStatement();
				QBObject ret = func.execute(new QBObject[]{});
				
				if( ret == null )
					ret = QBMaybe.new$0x20Null();
				
				System.out.println("?: " + ret);
				this.getGlobalNamespace().set("ans", ret);
				
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private int depth = 0;
	public TransparentFunction readStatement() throws Exception{
		System.out.print(">>> ");
		StringBuffer tot = new StringBuffer();
		
		tot.append(readLine());
		while(tot.length() == 0){
			System.out.print(">>> ");
			tot.append(readLine());
		}
		
		while( depth > 0 ) {
			System.out.print("... ");
			tot.append(readLine());
		}
		
		QBStatement sup = alwaysSuper.clone();
		sup.clearChildren();
		QBStatement tmp;
		
		try{
			tmp = parser.parseCode(tot.toString());
		} catch( RuntimeException e ) {
			tmp = parser.parseCode( tot.toString() + ";" );
		}
		
		for( Statement stmt : tmp.getChildren()){
			sup.addChild(stmt);
		}
		
		return new TransparentFunction(sup, parser, this);
	}
	
	public String readLine() throws IOException{
		String ret = console.readLine();
		
		if( ret == null ) {
			System.out.println("Goodbye.");
			System.exit(0);
		}
		
		int quotes = 0;
		
		for( int i = 0;i < ret.length();i++) {
			if(ret.charAt(i) == '"')
				quotes ++;
			
			if(quotes % 2 == 1)
				continue;
			
			if(ret.charAt(i) == '{') {
				depth ++;
			} else if( ret.charAt(i) == '}') {
				depth --;
			}
		}
		
		return ret;
	}
	

}
