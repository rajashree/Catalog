package com.java.usorted;

import java.util.StringTokenizer;

public class STokenizer {
	
	static String s = "Name:Devashree;D/O:K.Meganathan;Address:#180/1, NT Sandra;City:Bangalore;";
	
	public static void main(String ar[]){
	
		StringTokenizer st= new StringTokenizer(s,";:");
		while(st.hasMoreTokens()){
			String key=st.nextToken();
			String value=st.nextToken();
			System.out.println(key +"\t"+ value);
		}
		
	}
	

}
