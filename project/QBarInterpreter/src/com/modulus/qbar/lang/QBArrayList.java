package com.modulus.qbar.lang;

import java.util.ArrayList;
import java.util.List;

import com.modulus.qbar.core.QBFunction;
import com.modulus.qbar.core.QBObject;
import com.modulus.qbar.core.QBStruct;

public class QBArrayList extends QBList{
	private List<QBObject> values = new ArrayList<QBObject>();
	
	public final static QBStruct arrayListStruct = new QBStruct("ArrayList");
	public static final QBFunction APPEND = new QBFunction(2) {
		
		@Override
		public QBObject execute(QBObject[] args) {
			QBArrayList lst = (QBArrayList)args[1];
			
			return lst.append(args[0]);
		}
	};
	
	static{
		arrayListStruct.addSuperStruct(QBList.list);
		arrayListStruct.set("++", APPEND);
	}
	
	
	public QBArrayList(QBObject[] objs){
		super(arrayListStruct);
		for( QBObject obj : objs )
			values.add(obj);
	}
	
	public QBArrayList(QBObject[] objs, QBStruct struct){
		super(struct);
		for( QBObject obj : objs )
			values.add(obj);
	}
	
	public QBArrayList() {
		this( new QBObject[]{} );
	}

	public void insert(int index, QBObject element) {
		values.add(index, element);
	}

	public QBObject add(QBObject e) {
		values.add(e);
		return this;
	}

	public void remove(QBObject arg0) {
		values.remove(arg0);
	}
	
	public QBObject get(int idx){
		if(idx < 0)
			return values.get(values.size()+idx);
		
		return values.get(idx);
	}

	public double size() {
		return values.size();
	}

	public QBArrayList append(QBObject obj){
		QBArrayList ret = new QBArrayList(new QBObject[]{});
		
		ret.values.addAll(this.values);
		
		if(obj instanceof QBArrayList)
			ret.values.addAll(((QBArrayList) obj).values);
		
		return ret;
	}
	
	public String toString(){
		return values.toString();
	}
	
	public Object getWrapped(){
		return values;
	}

	@Override
	public QBList realSubList(int off, int len) {
		QBArrayList ret = new QBArrayList();
		
		for( int i = off, a = 0; a < len;i ++, a ++)
			ret.add( this.get(i) );
		
		return ret;
	}
}
