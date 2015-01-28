package com.modulus.qbar.core.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.modulus.dataread.expressions.Statement;
import com.modulus.dataread.expressions.StatementFactory;
import com.modulus.dataread.expressions.StatementFormatter;
import com.modulus.dataread.expressions.impl.CBasedParseRules;
import com.modulus.dataread.expressions.impl.SimpleStatementTreeParser;
import com.modulus.qbar.core.QBConstructor;
import com.modulus.qbar.core.QBFile;
import com.modulus.qbar.core.QBFunction;
import com.modulus.qbar.core.QBNamespace;
import com.modulus.qbar.core.QBStruct;
import com.modulus.qbar.core.QBSyntheticFunction;
import com.modulus.qbar.core.QBSyntheticStruct;
import com.modulus.qbar.core.QBSyntheticUtilityNamespace;
import com.modulus.qbar.core.QBUtilityNamespace;
import com.modulus.qbar.core.exceptions.ParseException;
import com.modulus.qbar.core.interpreter.Main;
import com.modulus.qbar.core.interpreter.QBInterpreter;
import com.modulus.qbar.core.parser.stmt.QBStatement;
import com.modulus.qbar.core.parser.stmt.QBStatements;
import com.modulus.qbar.integration.QBWrappedClass;
import com.modulus.qbar.integration.QBWrappedUtilityNamespace;

public class QBParser {
	private static Set<Object> alreadyImported = new HashSet<Object>();
	
	private Core systemCore = new Core();
	private SimpleStatementTreeParser treeParser;
	private static List<AnnotationReader> annotationReaders = new ArrayList<AnnotationReader>();
	private QBNamespace currentCompilingNamespace;
	private QBInterpreter thisInterpreter;
	private Map<QBStruct, String> waitingListForExtending = new HashMap<QBStruct, String>();
	
	public QBParser( QBInterpreter interpreter ){
		thisInterpreter = interpreter;
	}
	
	public static final StatementFormatter formatter = new StatementFormatter() {
		private static final long serialVersionUID = 9033830509672123664L;

		@Override
		public String formatHeader(String header) {
			header = header.replaceAll("\\s+", " "); // only one space in needed
			header = header.trim(); // trim it to look better
			header = header.replaceAll("\\s+\\(", "("); // remove spaces before parenthasis
			
			return header;
		}
	};

	public static final StatementFactory<QBStatement> PARSER_STATEMENT_FACTORY = new StatementFactory<QBStatement>() {
	
				@Override
				public QBStatement generateStatement(Map<String, Object> params) {
					return new QBStatement(formatter);
				}
				
	};
	
	
	public QBStatement parseCode( String code ){
		// FIXME big use of bandaid
		code = "Namespace global{\n" + code + "\n}";
		CBasedParseRules rules = new CBasedParseRules(code){
			@Override
			public String getCommentOpen() {
				return "-{";
			}
			
			@Override
			public String getCommentClose() {
				return "}-";
			}
			
			@Override
			public String getLineCommentDelimeter() {
				return "--";
			}
		};
		
		treeParser = new SimpleStatementTreeParser(rules);
		
		QBStatement statements = (QBStatement) treeParser.parseStatements( PARSER_STATEMENT_FACTORY);
		
		for(Statement stmt : statements.getChildren() ){
			if(stmt.getHeader().startsWith("@")){
				readAnnotation( stmt );
			} else{
				readStatement(stmt, systemCore.getGlobalNamespace());
			}
		}
		
		return statements;
	}
	
	public static void readAnnotation(Statement stmt) {
		for(AnnotationReader annotReader : annotationReaders)
			annotReader.readAnnotation(stmt);
	}
	
	public static void addAnnotationReader(AnnotationReader reader){
		annotationReaders.add(reader);
	}

	public  void readStatement( Statement stat, QBNamespace nmspce ){
		//System.out.println("HEADER " + stat.getHeader());
		currentCompilingNamespace = nmspce;
		
		if(stat instanceof QBStatement)
			((QBStatement) stat).compile( this );
		
		try{
			if(QBStatements.stmtIsImport(stat)){
				Main.debug(QBParser.class, "Importing Stuff!");
				try {
					importStuff(stat, nmspce);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			} else if(QBStatements.stmtIsLoad(stat)){
				try {
					loadStuff(stat, nmspce);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			} else if( QBStatements.stmtIsConstructor(stat) ){
				stat.setHeader( stat.getHeader().replaceAll("new\\s+", "new\\$0x20") );
				QBSyntheticFunction constructor = new QBConstructor((QBStatement) stat, (QBStruct) nmspce, this);
				constructor.setSuper(nmspce);
				systemCore.getGlobalNamespace().set(constructor.getName(), constructor);
			} else if( QBStatements.stmtIsFunction(stat) ){
				QBFunction func = createFunction(stat, nmspce);
				
				if(nmspce instanceof QBSyntheticFunction) {
					((QBSyntheticFunction) nmspce).addSubObject( func.getName(), func);
				} else {
					nmspce.set(func.getName(), func);
				}
			} else if( QBStatements.stmtIsClass(stat) ){
				QBStruct struct = new QBSyntheticStruct(stat, this, nmspce);
				
				if(nmspce instanceof QBSyntheticFunction) {
					((QBSyntheticFunction) nmspce).addSubObject( struct.getName(), struct);
				} else {
					nmspce.set(struct.getName(), struct);
				}
			} else if( QBStatements.stmtIsNamespace(stat)){
				QBUtilityNamespace utilNamespace = new QBSyntheticUtilityNamespace(stat, this, nmspce);
				utilNamespace.construct();
				
				if(nmspce instanceof QBSyntheticFunction) {
					((QBSyntheticFunction) nmspce).addSubObject( utilNamespace.getName(), utilNamespace);
				} else {
					nmspce.set(utilNamespace.getName(), utilNamespace);
				}
			}
		} catch(Exception e){
			e.printStackTrace();
			throw new ParseException(stat);
		}
	}

	public QBFunction createFunction(Statement stat, QBNamespace nmspce) {
		QBFunction func = new QBSyntheticFunction((QBStatement) stat, this);
		func.setSuper(nmspce);
		return func;
	}
	
	public QBFunction createInteractiveFunction(Statement stat, QBNamespace nmspce) {
		QBFunction func = new QBSyntheticFunction((QBStatement) stat, this);
		func.setSuper(nmspce);
		return func;
	}
	
	private   void importStuff(Statement importStmt, QBNamespace nmspce) throws IOException, ClassNotFoundException{
		String[] headers = importStmt.getHeader().split("\\s+", 3);
		
		String next = headers[1];
		if(next.equals("Native"))
			importNative( headers[2], nmspce );
		
		else
			importStandard(headers[1]);
	}
	
	private  void loadStuff(Statement importStmt, QBNamespace nmspce) throws IOException, ClassNotFoundException{
		String[] headers = importStmt.getHeader().split("\\s+", 3);
		
		String next = headers[1];
		if(next.equals("Native"))
			loadNative( headers[2], nmspce );
		
		else
			loadStandard(headers[1], nmspce);
	}
	
	private  void loadStandard(String string, QBNamespace ns) {
	//	System.out.println("Incorporating: " + ns);
		ns.incoporate( ns.get(string) );
	}

	private  void loadNative(String clazz, QBNamespace nmspce) throws ClassNotFoundException {
		QBNamespace tmp = importNative(clazz, nmspce);
		
		nmspce.incoporate(tmp);
	}

	private  void importStandard(String file) throws IOException, ClassNotFoundException{

		
		File f = QBFile.getQBFile(file);
		
		if(alreadyImported.contains(f.getAbsolutePath())){
			Main.debug(this.getClass(), f.getAbsolutePath() + " is already imported!");
			return;
		}
		alreadyImported.add(f.getAbsolutePath());
		
		QBFileType type = QBFileType.getFileType(file);
		QBInterpreter interpreter = null;
		FileInputStream in = new FileInputStream(f);
		
		// TODO fix to use enum to read
		switch(type){
		case SOURCE:
			interpreter = QBInterpreter.readInterpreterFromSource( in );
			break;
			
		case COMPILED:
			interpreter = QBInterpreter.readInterpreter( in );
			break;
			
		case GZIPPED:
			interpreter = QBInterpreter.readInterpreterCompressed( in );
		}
		
		QBInterpreter.instance().merge(interpreter);
	}
	
	private  QBNamespace importNative(String clazz, QBNamespace nmspce) throws ClassNotFoundException{
		Pattern p = Pattern.compile("\\s+as\\s+");
		Matcher m = p.matcher(clazz);
		
		String name = clazz.substring(clazz.lastIndexOf('.') + 1);
		
		if( m.find() ){
			String[] split = clazz.split("\\s+as\\s+");
			clazz = split[0];
			
			name = split[1];
		}

		Class<?> cls = thisInterpreter.getClassLoader().loadClass(clazz);//Class.forName(clazz);
		QBWrappedUtilityNamespace utilityNamespace = new QBWrappedUtilityNamespace( name );
		
		QBStruct struct = QBWrappedClass.wrapClass(cls, utilityNamespace);
		struct.setSuper(nmspce);
		
		utilityNamespace.set(struct.getName(), struct);
		nmspce.set(utilityNamespace.getName(), utilityNamespace);
		
		return utilityNamespace;
	}
	
	
	public  Core getSystemCore(){
		return systemCore;
	}
	
	public  void resetCore(){
		systemCore = new Core();
	}

	public void setCurrentCompilingNamespace(
			QBNamespace currentCompilingNamespace) {
		this.currentCompilingNamespace = currentCompilingNamespace;
	}

	public QBNamespace getCurrentCompilingNamespace() {
		return currentCompilingNamespace == null ? systemCore.getGlobalNamespace() : currentCompilingNamespace;
	}
	
	public void insertIntoWaiting(QBStruct struct, String other){
		waitingListForExtending.put(struct, other);
	}
	
	public QBInterpreter getInterpreter(){
		return this.thisInterpreter;
	}
}
