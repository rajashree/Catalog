package com.java.usorted;

import java.util.Date;
class A
{
	int i=0;
	String str;
       static int  z; 	
}
public class Runintf extends A implements Runnable{
	public Runintf(String st)
	{
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
	        		     z++;
	        			 System.out.println("Hello !"+i+"****z is"+z);
	        			 if(i==5)
	        				Thread.currentThread().stop();
	        		     Thread.sleep(3000);
	        		 }
	        		 else
	        		 {
	        			System.out.println("Time is"+new Date()+"i val is "+i+" ****z is "+z);
	        			 Thread.sleep(1000);
	        		 }
	        	 }
	        	 catch(InterruptedException ep)
	        	 {
	        		 
	        	 }
	         }
	}
	public static void main(String[] args) {
              Runintf rf1=new Runintf("first");
              Runintf rf2=new Runintf("second");
              Thread t1= new Thread(rf1);
              Thread t2=new Thread(rf2);
              t1.start();
             
              t2.start();
              

	}

}
