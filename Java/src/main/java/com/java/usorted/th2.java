package com.java.usorted;

public class th2  implements Runnable {

	
	
	public void run() {
		System.out.println("hello Thread");
		
		} 
		
		
	
public static void main(String[] args) {
		th2 t2=new th2();
		Thread t4=new Thread(t2);
		
		th2 t3=new th2();
		t3.run();
	
	
	}


}
