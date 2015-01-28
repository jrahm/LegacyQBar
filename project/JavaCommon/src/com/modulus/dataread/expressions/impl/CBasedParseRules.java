package com.modulus.dataread.expressions.impl;

public class CBasedParseRules extends AbstractParseRules{

	public CBasedParseRules(String code) {
		super(code);
	}

	@Override
	public String getOpenFolderString() {
		// TODO Auto-generated method stub
		return "{";
	}

	@Override
	public String getCloseFolderString() {
		// TODO Auto-generated method stub
		return "}";
	}

	@Override
	public String getStatementTerminatingCharacter() {
		// TODO Auto-generated method stub
		return ";";
	}

	@Override
	public String getStringDelimiter() {
		// TODO Auto-generated method stub
		return "\"";
	}

	@Override
	public String getEscapeSequence() {
		// TODO Auto-generated method stub
		return "\\";
	}

	@Override
	public String getCommentOpen() {
		// TODO Auto-generated method stub
		return "/*";
	}

	@Override
	public String getCommentClose() {
		// TODO Auto-generated method stub
		return "*/";
	}

	@Override
	public String getLineCommentDelimeter() {
		// TODO Auto-generated method stub
		return "//";
	}
	
}