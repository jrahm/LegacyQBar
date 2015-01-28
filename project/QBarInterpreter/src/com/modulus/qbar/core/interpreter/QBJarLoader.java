package com.modulus.qbar.core.interpreter;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class QBJarLoader {
	private ClassLoader loader;
	
	public QBJarLoader( String dir ) throws MalformedURLException{
		this( new File(dir) );
	}
	
	public QBJarLoader( File dir ) throws MalformedURLException{
		loader = new URLClassLoader( listJarUrls(dir), ClassLoader.getSystemClassLoader() );
	}
	
	public void load(){
	}
	
	public ClassLoader getLoader(){
		return loader;
	}
	
	private URL[] listJarUrls( File dir ) throws MalformedURLException{
		if( dir.isFile() ){
			return new URL[]{ dir.toURI().toURL() };
		} else {
			File[] files = dir.listFiles( new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".jar");
				}
			});
			
			URL[] ret = new URL[ files.length ];
			
			for( int i = 0;i < files.length; i ++)
				ret[i] = files[i].toURI().toURL();
			
			return ret;
		}
	}
}
