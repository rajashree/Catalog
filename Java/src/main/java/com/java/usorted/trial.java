package com.java.usorted;

import java.io.File;
import java.util.StringTokenizer;



public class trial {
	static String  getClassesFolderPath(Class clsObject){
    	String fullPath = trial.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    	
    	String className = clsObject.getName();
    	String classNameChanged = replace( className , "." , "/" );
    	int pos;
    	if( (pos = fullPath.indexOf(classNameChanged)) > 0  ){
    		fullPath = fullPath.substring( 0, pos );
    	}
    	
    	return new File(fullPath).getParentFile().getAbsolutePath();
    }

static String replace(String org,String find,String repl){
		StringBuffer sb = new StringBuffer(org);
		int pos;
		while( (pos =  sb.indexOf(find)) >= 0  ){
			sb.replace(pos,pos+find.length(),repl);
		}
		
		return sb.toString();
}
public static void main(String args[]){
	
    
    
/*
    		String path = getClassesFolderPath(trial.class);
    		System.out.println("path" + path);
    		System.setProperty("com.rdta.dhforms.path", path );
	
    		String propVal = System.getProperty("com.rdta.dhforms.path");*/
  		String propVal = "C:\\jakarta-tomcat-5.0.29\\webapps\\axis\\WEB-INF";
			StringTokenizer st= new StringTokenizer(propVal,"%20");
    		String key=st.nextToken();
    		while(st.hasMoreTokens()){
    			key+=" "+st.nextToken();
    			
    		}
    		System.out.println(key);
    		 String[][] a2;
			 // print array in rectangular form
			 for (int r=0; r<10; r++) {
				//a2= "tagName";
				//a2= "tagCount1";
			 }
				//for(int r=0;r<a2;r++){}

    		/*
	r++){}tring[] a =	propVal.split("%20");
	    	String value  = a[0];	
	    	for(int i=1;i<a.length;i++){
	    			value+=" "+a[i];
	    	}
	    	System.out.println("*********"+value);*/
}}