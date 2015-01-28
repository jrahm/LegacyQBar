package com.modulus.dataread.expressions.impl;

import com.modulus.dataread.expressions.ParseRules;

/**
 * Class which implements ParseRules with rules
 * of a language which is very close to the C Programming
 * Language and others based off it.
 * 
 * @author jrahm
 */
public abstract class AbstractParseRules implements ParseRules{
	private static final String w = "\\s";
	
	private int lineNumber = 0;
	/*
	 * the code this class will follow
	 */
	private String code;
	/*
	 * value which tells this class
	 * if it is in quotes or not.
	 */
	private boolean inQuotes = false;
	
	/*
	 * value which dictates if the parser
	 * is in comments.
	 */
	private boolean inComments = false;
	
	/*
	 * this tells if the parser is in
	 * a comment delimited by '//'
	 */
	private boolean inLineComment = false;
	/**
	 * Constructs a new CBasedParseRules object with <code>code</code>
	 * as the code which is being parsed.
	 * 
	 * @param code the code to be parsed.
	 */
	public AbstractParseRules(String code){
		this.code = code;
		format();
		
		deleteComments();
	}
	
	@Override
	public boolean closeFolder(int off) {

		return code.startsWith(this.getCloseFolderString(), off);
	}

	@Override
	public boolean inComment() {
		return inComments || inLineComment;
	}

	@Override
	public boolean inQuotes() {
		return inQuotes;
	}

	@Override
	public boolean openFolder(int off) {
		return code.startsWith( this.getOpenFolderString(), off );
	}
	
	/*
	 * this is where most of the logic
	 * goes for this class, this method is
	 * what dictates the flow of the parser
	 * and whether or not the parser is in
	 * quotes or comments etc.
	 * 
	 * @see com.modulus.dataread.expressions.ParseRules#read(int)
	 */
	@Override
	public int read(int off) {
		// Here we will handle to see if the parser
		// has run into a string literal.
		
		// System.out.print( code.charAt(off) );
		if(code.charAt(off) == '"'){
			if( off == 0 || code.charAt(off - 1) != '\\' ){
				inQuotes = !inQuotes;
			}
		}
		
		return 1;
	}
	
	public boolean statementTerminated( int off ){
		boolean tmp = code.startsWith( this.getStatementTerminatingCharacter(), off ) && !inQuotes();
		
	//	if(tmp) {
	//		System.out.printf("%s\t%s\t%s\n", code.substring(off), off, inQuotes );
	//	}
		
		return tmp;
	}

	@Override
	public void format() {
		// for most C-based languages white space can just be
		// Truncated like so.
	//	this.code = this.code.replaceAll(w+"+\n"+w+"+", "\n");
		this.code = this.code.replace('\n', '\u0088');
		// this.code = this.code.replaceAll(w+"+", " ");
		// this.code = this.code.replaceAll(w+"*\\}"+w+"*", "}");
		// this.code = this.code.replaceAll(w+"*;"+w+"*", ";");
		StringBuffer total = new StringBuffer();
		
		int i = 0;
		int old = 0;
		
		boolean inQuotes = false;
		
		while( i != this.code.length() ) {
			old = i;
			i = code.indexOf('"', i+1);
			
			if( i == -1 )
				i = this.code.length();
			
			String sub = code.substring(old, i);
			
			if(!inQuotes) {
				sub = sub.replaceAll(w+"+", " ");
				sub = sub.replaceAll(w+"*\\}"+w+"*", "}");
				sub = sub.replaceAll(w+"*;"+w+"*", ";");
			}
			
			total.append(sub);
			
			inQuotes = !inQuotes;
		}
		total.append( code.substring(old) );
		
		code = total.toString();
		//this.code = this.code.replaceAll("[^;]\\}", ";}");
		
	//	System.out.println("Format: " + this.code);
	}
	
	public String getCode(){
		return code.replace('\u0088', '\n');
	}

	@Override
	public int getLineNumber() {
		return lineNumber;
	}
	
	private void deleteComments(){
		StringBuffer code2 = new StringBuffer();
		
		for( int i = 0;i < code.length(); i ++){
			if(code.startsWith( this.getCommentOpen(), i )){
				while(!code.startsWith( this.getCommentClose(), i)){
					if(code.charAt(i) == '\u0088')
						code2.append(code.charAt(i));
					i ++;
				}
				i += this.getCommentClose().length();
			}
			
			if(code.startsWith( this.getLineCommentDelimeter(), i)){
				while(code.charAt(i) != '\u0088'){
					i++;
				}
			}
			
			code2.append( code.charAt(i) );
		}
		
		code = code2.toString();
	}
	
	public abstract String getOpenFolderString();
	
	public abstract String getCloseFolderString();
	
	public abstract String getStatementTerminatingCharacter();
	
	public abstract String getStringDelimiter();
	
	public abstract String getEscapeSequence();
	
	public abstract String getCommentOpen();
	
	public abstract String getCommentClose();
	
	public abstract String getLineCommentDelimeter();
}
