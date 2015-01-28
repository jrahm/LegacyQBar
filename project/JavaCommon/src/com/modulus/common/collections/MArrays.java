package com.modulus.common.collections;

import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.List;

public class MArrays {
	public static int indexOf( Object[] objs , Object obj, int start ){
		for ( int i = start; i < objs.length; i ++)
			if(objs[i].equals(obj))
					return i;
		return -1;
	}
	
	public static int lastIndexOf( Object[] objs , Object obj, int end ){
		for ( int i = end; i >= 0; i --)
			if(objs[i].equals(obj))
					return i;
		return -1;
	}
	
	public static int indexOf( Object[] objs, Object obj ){
		return indexOf( objs, obj, 0 );
	}
	
	public static int lastIndexOf( Object[] objs, Object obj ){
		return lastIndexOf( objs, obj, objs.length - 1 );
	}
	
	public static String concat(String[] args, int off, int len, String sep){
		StringBuffer buf = new StringBuffer();
		int lastDex = len + off;
		
		for(int i = off;i < lastDex-1;i++)
			buf.append(args[i] + sep);
		buf.append(args[lastDex-1]);
	
		return buf.toString();
	}
	
	public static String concat(String[] args, int off, String sep){
		return concat(args, off, args.length-off, sep);
	}
	
	public static String concat(String[] args, String sep){
		return concat(args, 0, args.length, sep);
	}
	
	public static String concat(String[] args, int off){
		return concat(args, off, "");
	}
	
	public static String concat(String[] args){
		return concat(args, "");
	}
	
	public static String concat(String[] args, int off, int len){
		return concat(args, off, len, "");
	}
	
	public static List<Byte> asList( final byte[] bytes ){
		return new AbstractList<Byte>() {

			@Override
			public Byte get(int arg0) {
				return bytes[arg0];
			}

			@Override
			public int size() {
				return bytes.length;
			}
		};
	}
	
	public static List<Character> asList( final char[] chars ){
		return new AbstractList<Character>() {

			@Override
			public Character get(int arg0) {
				return chars[arg0];
			}

			@Override
			public int size() {
				return chars.length;
			}
		};
	}
	
	public static List<Integer> asList( final int[] ints ){
		return new AbstractList<Integer>() {

			@Override
			public Integer get(int arg0) {
				return ints[arg0];
			}

			@Override
			public int size() {
				return ints.length;
			}
		};
	}
	
	public static List<Long> asList( final long[] longs ){
		return new AbstractList<Long>() {

			@Override
			public Long get(int arg0) {
				return longs[arg0];
			}

			@Override
			public int size() {
				return longs.length;
			}
		};
	}
	
	public static List<Double> asList( final double[] doubles ){
		return new AbstractList<Double>() {

			@Override
			public Double get(int arg0) {
				return doubles[arg0];
			}

			@Override
			public int size() {
				return doubles.length;
			}
		};
	}
	
	public static List<Float> asList( final float[] floats ){
		return new AbstractList<Float>() {

			@Override
			public Float get(int arg0) {
				return floats[arg0];
			}

			@Override
			public int size() {
				return floats.length;
			}
		};
	}
	
	public static List<Short> asList( final short[] shorts ){
		return new AbstractList<Short>() {

			@Override
			public Short get(int arg0) {
				return shorts[arg0];
			}

			@Override
			public int size() {
				return shorts.length;
			}
		};
	}
	
	public static List<Boolean> asList( final boolean[] bools ){
		return new AbstractList<Boolean>() {

			@Override
			public Boolean get(int arg0) {
				return bools[arg0];
			}

			@Override
			public int size() {
				return bools.length;
			}
		};
	}
}
