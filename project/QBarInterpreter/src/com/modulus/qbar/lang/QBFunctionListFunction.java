package com.modulus.qbar.lang;

import java.util.HashMap;
import java.util.Map;

import com.modulus.common.collections.ArrayStack;
import com.modulus.common.collections.Stack;
import com.modulus.qbar.core.QBFunction;
import com.modulus.qbar.core.QBNamespace;
import com.modulus.qbar.core.QBObject;
import com.modulus.qbar.core.interpreter.QBInterpreter;
import com.modulus.qbar.core.parser.ByteOperation;

public class QBFunctionListFunction extends QBFunction{
	private static final long serialVersionUID = 1123773383702904111L;
	private ByteOperation[] command;
	private String var;
	private static Map<String, QBObject> namespaceTemplate = new HashMap<String,QBObject>();
	private Stack<QBNamespace> namespaceCalls = new ArrayStack<QBNamespace>();
	
	public QBFunctionListFunction( ){
		super(2);
		namespaceCalls.push(new QBNamespace(namespaceTemplate));
	}
	
	public ByteOperation[] getCommand() {
		return command;
	}

	public void setCommand(ByteOperation[] command) {
		this.command = command;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	@Override
	public QBObject execute(QBObject[] args){
		QBNamespace tmp = new QBNamespace(new HashMap<String, QBObject>());
		tmp.setSuper(this.getSuper());
		
		namespaceCalls.push(tmp);
		this.set(var, args[0]);
		this.set("this", args[1]);
		Stack<QBObject> stack = QBInterpreter.instance().getStack();
		
		exec(command, stack);
		namespaceCalls.pop();
		return returnedValue;
	}
	
	@Override
	public QBObject get(String name){
		return namespaceCalls.peek().get(name);
	}
	
	@Override
	public void set(String name, QBObject obj){
		namespaceCalls.peek().set(name, obj);
	}
}
