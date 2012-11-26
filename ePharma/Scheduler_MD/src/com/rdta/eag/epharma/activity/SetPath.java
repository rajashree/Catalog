package com.rdta.eag.epharma.activity;

import java.io.File;

public class SetPath {



	static String  getClassesFolderPath(Class clsObject){
    	String fullPath = SetPath.class.getProtectionDomain().getCodeSource().getLocation().getPath();

    	String className = clsObject.getName();
    	String classNameChanged = replace( className , "." , "/" );
      /* int pos;
    	if( (pos = fullPath.indexOf(classNameChanged)) > 0  ){
    		fullPath = fullPath.substring( 0, pos );
    	}*/

    	return new File(fullPath).getAbsolutePath();
    }

    static String replace(String org,String find,String repl){
    	StringBuffer sb = new StringBuffer(org);
    	int pos;
    	while( (pos =  sb.indexOf(find)) >= 0  ){
    		sb.replace(pos,pos+find.length(),repl);
    	}

    	return sb.toString();
    }
    static{
    	try{
    		String path = getClassesFolderPath(SetPath.class);
    		System.out.println("Set the PedigreeBank Path as  " + path);
    		System.setProperty("com.rdta.pedigreebank.path", path );
    	}catch(Exception e){
    		System.out.println("Exception while setting the PedigreeBank Path");
    	}
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub



	}

}
