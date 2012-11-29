package com.java.usorted;


import java.io.*;

public class ArrayStringInput {
	public String Result() throws IOException{
		

		String s="String";
		StringBuffer sb=new StringBuffer();
		StringBufferInputStream sis=null;
		try
		{
			sis=new StringBufferInputStream(s);
		    byte b[]=new byte[4096];
		    int n=sis.read(b);
		    System.out.println(" n = "+n);
		    sb.append(new String(b,0,n));
			System.out.println(sb.toString());
		}
		catch(Exception e){ System.out.println(e);}
		finally
		{
			sis.close();
		}
		return s;
}

}
