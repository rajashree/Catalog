package com.java.usorted;

import java.util.Date;

public class Mthread extends Thread {
	String str;
     int i=0;    
	public Mthread(String st) {
               str=st;
	}
	public void run()
	{
	         while(true)
	         {
	        	 System.out.println("Thread name is"+str);
	        	 try
	        	 {
	        		 if(str.equals("first"))
	        		 {
	        		     i++;
	        			 System.out.println("Hello Prasanthi!"+i);
	        			 if(i==5)
	        				this.stop();
	        		     Thread.sleep(3000);
	        		 }
	        		 else
	        		 {
	        			System.out.println("Time is"+new Date()+"i val is "+i);
	        			 Thread.sleep(1000);
	        		 }
	        	 }
	        	 catch(InterruptedException ep)
	        	 {
	        		 
	        	 }
	         }
	}
}

