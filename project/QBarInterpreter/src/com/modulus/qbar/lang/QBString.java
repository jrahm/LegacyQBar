package com.modulus.qbar.lang;

import java.util.HashMap;
import java.util.Map;

import com.modulus.qbar.core.QBObject;
import com.modulus.qbar.core.QBStruct;
import com.modulus.qbar.core.exceptions.ParseException;
import com.modulus.qbar.core.primitive.QBChar;

public class QBString extends QBArrayList {
	
	public static final QBStruct string = new QBStruct("String", QBList.list){
		
	};
	
	private static Map<String, String> escapes = new HashMap<String, String>();
	
	static{
		string.addSuperStruct(QBArrayList.arrayListStruct);
		
		escapes.put("\\n", "\n");
		escapes.put("\\\\", "\\");
		escapes.put("\\r", "\r");
		escapes.put("\\t", "\t");
		escapes.put("\\\"", "\"");
		escapes.put("\\'", "'");
	}
	
	public QBString(QBChar[] objs) {
		super(objs, string);
	}
	
	public QBString(String str, boolean parse){
		this( dynamicCast(str,parse) );
	}
	
	@Override
	public String getWrapped(){
		StringBuffer buf = new StringBuffer();
		
		for(int i = 0;i < this.size();i++)
			buf.append( this.get(i) );
		
		return buf.toString();
	}

	private static QBChar[] dynamicCast(String str, boolean parse){
		if(parse)
			str = parseString(str);
		
		char[] chars = str.toCharArray();
		QBChar[] chars2 = new QBChar[chars.length];
		
		for(int i =0 ;i < chars.length;i++)
			chars2[i] = new QBChar(chars[i]);
		
		return chars2;
	}
	
	public String toString(){
		return this.getWrapped();
	}
	
	@Override
	public QBArrayList append(QBObject obj){
		String str1 = this.toString();
		String str2 = obj.toString();
		
		return new QBString(str1 + str2, false);
	}
	
	@Override
	public QBObject add(QBObject obj){
		String str = obj.toString();
	
		for(int i = 0;i < str.length();i++){
			super.add( new QBChar(str.charAt(i)) );
		}
		
		return this;
	}
	
	private static String parseString(String str){
		StringBuffer buf = new StringBuffer();
		boolean escaped = false;
		for( int i = 0;i < str.length();i ++){
			if(str.startsWith("\\", i)){
				for ( String key : escapes.keySet() ){
					// System.out.printf("str.startsWith( %s, %s )\n", key, i);
					if( str.startsWith(key, i) ){
						buf.append( escapes.get(key) );
						i++;
						
						escaped = true;
						break;
					}
				}
			} 
			
			if(!escaped){
				buf.append(str.charAt(i));
			} else {
				escaped = false;
			}
		}
		
		return buf.toString();
	}

}
