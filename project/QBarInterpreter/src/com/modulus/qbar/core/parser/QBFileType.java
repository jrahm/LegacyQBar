package com.modulus.qbar.core.parser;

import com.modulus.qbar.core.interpreter.CompiledReader;
import com.modulus.qbar.core.interpreter.CompressedReader;
import com.modulus.qbar.core.interpreter.QBInterpreterReader;
import com.modulus.qbar.core.interpreter.SourceReader;

public enum QBFileType {
	SOURCE(".qbar", new SourceReader()),
	COMPILED(".qbc", new CompiledReader()),
	GZIPPED(".qbz", new CompressedReader());
	
	private String ext;
	private QBInterpreterReader reader;
	
	QBFileType(String ext, QBInterpreterReader reader){
		this.ext = ext;
		this.reader = reader;
	}
	
	public String getExtention(){
		return ext;
	}
	
	public QBInterpreterReader getReader(){
		return this.reader;
	}
	
	public static QBFileType getFileType(String file){
		for( QBFileType type : QBFileType.values() )
			if(file.endsWith(type.getExtention()))
				return type;
			
		
		return SOURCE;
	}
}
