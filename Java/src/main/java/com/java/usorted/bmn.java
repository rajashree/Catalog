package com.java.usorted;

public class bmn {
	public static void main(String[] arg){


	
new Thread(new Runnable() {
		  public void run() {
		    while (true) {
		    	try {
					Thread.sleep(1000);
					 System.out.print("hello");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
		        
		      }
		  }
		}).start();
	
	 
	}
	
}
