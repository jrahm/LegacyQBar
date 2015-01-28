package com.modulus.common.strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class that tokenizes strings based on which groups
 * the characters fit into.
 * 
 * If the group is -1, then that tells the tokenizer to
 * not include those tokens and instead delete the characters
 * that belong to that group in the process of splitting.
 * 
 * @author jrahm
 *
 */
public abstract class Tokenizer {
	
	public String[] tokenize(String str){
		if(str.length() == 0)
			return new String[]{};
		
		List<String> tokens = new ArrayList<String>();
		StringBuffer buffer = new StringBuffer();
		
		int curGroup = groupOf(str.charAt(0));
		for(int i = 0;i < str.length();i++){
			char ch = str.charAt(i);
			
			int temp = groupOf(ch);
			if(temp != curGroup && curGroup != -1){
				curGroup = temp;
				tokens.add(buffer.toString());
				
				buffer = new StringBuffer();
			}
			
			if(temp != -1)
				buffer.append(ch);
		}
		tokens.add(buffer.toString());
		
		return tokens.toArray(new String[tokens.size()]);
	}
	
	public abstract int groupOf(char ch);
}
