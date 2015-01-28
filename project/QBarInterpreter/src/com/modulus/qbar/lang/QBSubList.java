package com.modulus.qbar.lang;

import com.modulus.qbar.core.QBObject;

public class QBSubList extends QBList {
	private static final long serialVersionUID = 8841750568883987599L;
	private QBList original;
	private boolean originalIsOriginal = true;
	
	private int offset;
	private int length;
	
	public QBSubList(QBList original, int offset, int length) {
		super();
		this.original = original;
		this.offset = offset;
		this.length = length;
	}
	
	private void checkOriginal(){
		if(originalIsOriginal){
			original = original.realSubList(offset, length);
			originalIsOriginal = false;
		}
	}
	
	@Override
	public QBObject add(QBObject obj) {
		checkOriginal();
		return original.add(obj);
	}

	@Override
	public void remove(QBObject obj) {
		checkOriginal();
		original.remove(obj);
	}

	@Override
	public QBObject get(int idx) {
		if(originalIsOriginal) {
			return original.get(idx + offset);
		} else {
			return original.get(idx);
		}
	}

	@Override
	public double size() {
		if( originalIsOriginal )
			return this.length;
		return original.size();
	}

	@Override
	public void insert(int idx, QBObject obj) {
		checkOriginal();
		original.insert(idx, obj);
	}

	@Override
	public QBList realSubList(int off, int len) {
		return original.realSubList(off + offset, len);
	}
	
	public String toString(){
		StringBuffer buf = new StringBuffer("[");
		
		for( int i = 0;i < this.size(); i ++)
			buf.append( this.get(i).toString() + ", ");
		
		buf.deleteCharAt(buf.length()-1);
		buf.deleteCharAt(buf.length()-1);
		
		return buf.toString() + "]";
	}
}
