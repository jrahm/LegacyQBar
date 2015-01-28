package com.modulus.qbar.lang;

public class QBSystem {
	public static int time(){
		return (int) System.currentTimeMillis();
	}
	
	public static void exit( int status ){
		System.exit( status );
	}
}
