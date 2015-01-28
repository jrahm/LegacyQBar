package com.modulus.common.collections;

public class Collections2 {
	
	static public Integer[] rangeAsObjects(int start, int stop)
	{
		Integer[] result = new Integer[stop-start];

	   for(int i=0;i<stop-start;i++)
	      result[i] = start+i;

	   return result;
	}
	
	static public int[] range(int start, int stop)
	{
	   int[] result = new int[stop-start];

	   for(int i=0;i<stop-start;i++)
	      result[i] = start+i;

	   return result;
	}
	
	static public int[] range(int stop){
		return range(0, stop);
	}
}
