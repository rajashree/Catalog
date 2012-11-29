package com.java.usorted;

public class th1 extends Thread implements Runnable {

	static int i=1;
	
	public void run() {
		
		while (true){
		
		
		try {
			if(Thread.currentThread().getName().equals("first"))
			{
				i++;
				System.out.println("firts thread and i is "+i);
				Thread.sleep(3000);
			}
				else
				{
					System.out.println("second thread and i is "+i);
				Thread.sleep(1000);
				}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
	}
public static void main(String[] args) {
		th1 a=new th1();
		th1 b=new th1();
	Thread t1=new Thread(a);
	t1.setName("first");
	Thread t2=new Thread(b);
	t2.setName("first");
	t1.start();
	t2.start();
	}


}
