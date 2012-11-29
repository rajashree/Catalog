package com.java.usorted;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class StrConvert{
	public static void main(String[] args) throws IOException{
		String name="Rajashree";
		ByteArrayInputStream bas=null;
		BufferedInputStream bis=null;
		byte b[]=new byte[1024];
		int i=0,ch;
		char ch1[]=new char[100];
		b=name.getBytes();
		bas=new ByteArrayInputStream(b);
		bis=new BufferedInputStream(bas);
		ch=bis.read();
		while(ch!=-1)
		{
				System.out.println((char)ch);
				ch1[i]=(char)ch;
				ch=bis.read();
				i++;
				
		}
		ch1[i]='\0';
		String convStr=new String(ch1);
		System.out.println("From Stream to string : "+convStr);
		
	}
}

/*
		String convStr=new String(ch1);
		System.out.println("From Stream to string  :"+convStr);
		}
		catch(IOException e)
		{
			System.out.println("Error in Reading ");
			e.printStackTrace();
		}
		finally
		{
			try{
			bis.close();
			bas.close();
			}catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}

}
*/