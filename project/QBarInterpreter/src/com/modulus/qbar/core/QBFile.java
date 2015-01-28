package com.modulus.qbar.core;

import java.io.File;

import com.modulus.qbar.core.interpreter.Main;

public class QBFile {
	private static String[] extensions = new String[]{"", ".qbz", ".qbc", ".qbar"};
	public static File getQBFile( String filename ){
		String[] folders = Main.getPath();
		
		if( folders == null )
			folders = new String[]{};
		
		File file = new File(filename);
		
		for(String path : folders){
		
			for(String str : extensions) {
				if(file.exists()) {
					Main.debug(QBFile.class, " Found File " + file + " on the classpath!");
					return file;	
				}
				
				
				file = new File(path, filename + str);
			//	Main.debug(QBFile.class, " Looking for File " + file.getAbsolutePath() + " on the classpath.");
			}
		}
		
		if(!file.exists())
			throw new RuntimeException("The file: " + filename + " cannot be found on the classpath!");
		
		return file;
	}
}
