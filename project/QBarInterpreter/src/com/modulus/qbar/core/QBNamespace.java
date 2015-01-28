package com.modulus.qbar.core;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * Class is the very root of all the interpreter classes.
 * a QBNamespace is anything that has the ability to store variables
 * including QBObjects and Functions.
 * 
 * @author jrahm
 *
 */
public class QBNamespace implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -102581163781603724L;

	private QBNamespace superNamespace;
	private String name = super.toString();
	// The map this namespace delegates,
	// This map can be of different types
	// like a HashTable.
	private Map<String, QBObject> map;
	
	/**
	 * Creates a new QBNamespace with the map <code>map</code>
	 * @param map
	 */
	public QBNamespace( Map<String, QBObject> map ){
		this( map, null );
	}
	
	public QBNamespace( Map<String, QBObject> map, QBNamespace sup){
		this.map = map;
		this.superNamespace = sup;
	}
	
	/**
	 * Sets the object with the name <code>name</code>
	 * to the specific <code>QBObject obj</code>
	 * 
	 * @param name the name of the variable to set
	 * @param obj the object to set that name equal to.
	 */
	public void set(String name, QBObject obj){
		this.map.put(name, obj);
	}
	
	/**
	 * Returns the object that is referenced by the
	 * string <code>name</code> from this Namespace
	 * 
	 * @param name the name of the object to retrieve
	 * @return the object referenced as <code>name</code>
	 */
	public QBObject get(String name){
	//	System.out.println("THIS: " + this);
		QBObject obj =  this.map.get(name);
		
		if(obj == null && superNamespace != null)
			obj = superNamespace.get(name);
		
		if(obj == null)
			throw new RuntimeException("The variable: " + name + " does not exist in the current namespace. " + this);
		
		return obj;
	}
	
	/**
	 * Returns the object that is referenced by the
	 * string <code>name</code> from this Namespace
	 * 
	 * This version of the function will not error
	 * if the object is not found
	 * 
	 * @param name the name of the object to retrieve
	 * @return the object referenced as <code>name</code>
	 */
	public QBObject getNoError(String name){
		QBObject obj =  this.map.get(name);
		
		if(obj == null && superNamespace != null)
			obj = superNamespace.getNoError(name);
		
		return obj;
	}
	
	public QBNamespace getSuper(){
		return this.superNamespace;
	}
	
	public void setSuper( QBNamespace sup ){
		this.superNamespace = sup;
	}
	
	public String toString2(){
		StringBuffer buf = new StringBuffer();
		for( String key : this.map.keySet() ){
			buf.append(key + ":\t");
			buf.append( this.get(key) + "\n");
		}
		
		return buf.toString();
	}
	
	public Set<String> getKeySet(){
		return map.keySet();
	}
	
	public boolean hasKey( String key ){
		return map.containsKey(key);
	}
	
	public QBObject hardGet(String[] args){
		QBNamespace cur = this;
		
		for(int i = 0;i < args.length - 1;i ++)
			cur = cur.get(args[i]);
		
		return cur.get(args[args.length - 1]);
	}
	
	public void hardSet(String[] args, QBObject obj){
		QBNamespace cur = this;
		
		for(int i = 0;i < args.length - 1;i ++)
			cur = cur.get(args[i]);
		
		cur.set(args[args.length - 1], obj);
	}
	
	/**
	 * Incorporates two namespaces and
	 * makes them clients to one and other,
	 * so that if something is added to one, 
	 * then it is added to the other.
	 * 
	 * @param other the other namespace
	 */
	public void incoporate( QBNamespace other ){
		load( other );
		// make the other one your bitch
		other.map = this.map;
	}
	
	/**
	 * Same as incorporates, except that in this case,
	 * the two namespaces remain sovereign and do not
	 * become clients.
	 * 
	 * @param other the other namespace
	 */
	public void load( QBNamespace other ){
		for (String key : other.getKeySet() ){
			this.set(key, other.get(key));
		}
	}
}
