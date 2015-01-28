package com.modulus.dataread.expressions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * this class outlines a rough diagram of what a statement
 * looks like. The major importance is how it already
 * correctly implements the tree fasion that statements
 * should in theory be ordered in.
 * 
 * @author jrahm
 *
 */
public abstract class AbstractStatement implements Statement, Serializable{
	private static final long serialVersionUID = -6717726729821743941L;
	private Collection<Statement> children;
	private int line = -1;
	
	/**
	 * the header of this statement.
	 */
	protected String header;
	
	/**
	 * Creates a new AbstractStatement with the
	 * default header being an empty string.
	 */
	public AbstractStatement(){
		this.children = new ArrayList<Statement>();
		this.header = "";
	}
	
	@Override
	public void addChild(Statement child) {
		this.children.add(child);
	}

	@Override
	public Statement[] getChildren() {
		return children.toArray(new Statement[children.size()]);
	}

	@Override
	public String getHeader() {
		return header;
	}

	@Override
	public void removeChild(Statement child) {
		this.children.remove(child);
	}
	
	@Override
	public String toString(){
		return toString(0);
	}
	
	public String toString( int recur ){
		StringBuffer buffer = new StringBuffer();
		String tab = "";
		for(int i = 0;i < recur;i++)
			tab += '\t';
		
		
		buffer.append(tab + header);
		
		if(children.size() > 0){
			buffer.append("{\n");
			for(Statement child : children){
				buffer.append(child.toString( recur + 1 ) + "\n");
			}
			buffer.append(tab + "}");
		} else{
			buffer.append(";");
		}
		
		return buffer.toString();
	}
	
	@Override
	public Statement getChildByHeader(String header){
		
		for(Statement child : children){
			String chHeader = child.getHeader();
			
			if(chHeader.equals(header))
				return child;
		}
		
		return null;
	}
	
	@Override
	public Statement[] getChildrenByHeader(String header){
		List<Statement> ret = new ArrayList<Statement>();
		
		for(Statement child : children){
			String chHeader = child.getHeader();
			
			if(chHeader.equals(header))
				ret.add(child);
		}
		
		return ret.toArray(new Statement[ret.size()]);
	}
	
	public int getLineNumber(){
		return line;
	}
	
	public void setLineNumber(int line){
		this.line = line;
	}
	
	public boolean hasChildren(){
		return !children.isEmpty();
	}
	
	public void clearChildren(){
		this.children.clear();
	}
}
