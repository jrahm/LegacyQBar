package com.modulus.qbar.core.parser.stmt;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import com.modulus.dataread.expressions.FlowStatement;
import com.modulus.dataread.expressions.Statement;
import com.modulus.dataread.expressions.StatementFormatter;
import com.modulus.dataread.expressions.impl.FormatStatement;
import com.modulus.qbar.core.exceptions.ParseException;
import com.modulus.qbar.core.parser.ByteOperation;
import com.modulus.qbar.core.parser.ExpressionCompiler;
import com.modulus.qbar.core.parser.ExpressionPuller;
import com.modulus.qbar.core.parser.QBParser;

/**
 * This class implements a statement that translates 
 * itself into more computer-readable code automatically.
 * It is guaranteed that all of the children of this
 * statement will be QBStatements as well, so it is safe to
 * cast.
 * 
 * @author jrahm
 *
 */
public class QBStatement extends FormatStatement implements Cloneable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1707466197323482810L;

	private static Collection<String> excludedHeaders = Arrays.asList("Struct", "let", "Namespace", "Import", "Incorporate");
	
	private ByteOperation[] byteOps;
	private FlowStatement flow = FlowStatement.NONE;
	
	public QBStatement(StatementFormatter format){
		super(format);
	}

	public ByteOperation[] getOperations(){
		return byteOps;
	}
	
	public FlowStatement getFlowStatement(){
		return flow;
	}
	
	private void p_compile(QBParser parser){
		try{
			
			String header = this.getHeader();
			header = ExpressionPuller.pullStrings(header);
			
			header = ExpressionPuller.pullLists(header, parser.getCurrentCompilingNamespace(), parser);
			
			header = header.replaceAll("new\\s+", "new\\$0x20");
			for( FlowStatement stmt : FlowStatement.values() ){
				if( header.startsWith(stmt.toString().toLowerCase()) ){
					if(!(stmt == FlowStatement.IF && header.contains("then"))){
						flow = stmt;
						
	
						header = header.substring(stmt.toString().length()).trim();
						if(stmt == FlowStatement.ELSE){
							byteOps = new ByteOperation[]{};
							return;
						}
						
						break;
					}
				}
			}
			
			ExpressionCompiler comp = new ExpressionCompiler();
			byteOps = comp.compile(header);
		} catch( RuntimeException e ){
			throw new ParseException(e.getMessage(), e, this);
		}
	}
	@Override
	public void setLineNumber(int line){
		//System.out.println(this.getHeader() + " : " + line);
		super.setLineNumber(line);
	}
	@Override
	public void setHeader( String header ){
		
		super.setHeader( header );
		if(this.getHeader().startsWith("}"))
			throw new RuntimeException("} as first character of header!");
		
	}
	
	public void compile( QBParser parser ){
		if(shouldCompile())
			p_compile( parser );
		
		for(Statement stmt : getChildren()){
			if(stmt instanceof QBStatement){
				QBStatement qbStmt = (QBStatement) stmt;
				qbStmt.compile( parser );
			}
		}
	}
	
	private boolean shouldCompile(){
		
		
		for(String s : excludedHeaders)
			if(this.getHeader().startsWith(s))
				return false;
		return true;
	}
	
	public QBStatement clone(){
		try {
			return (QBStatement) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
}
