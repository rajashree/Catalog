package com.java.usorted;

class NewThread implements Runnable{
	String name;
	Thread t;
	NewThread(String threadname){
		name = threadname;
		t=new Thread(this, name);
		System.out.println("New Thread "+t);
		t.start();
		
	}
	public void run(){
		try{
			for(int i=5;i>0;i--){
				System.out.println(name+":"+i);
				Thread.sleep(1000);
				
			}
		}catch(InterruptedException e){System.out.println(name+" is interrupted");
		}
		System.out.println(name +"is exiting");
	}
	
	
}
class thread{
	public void run(){
		try{
			for(int i=5;i>0;i--){
				System.out.println(+i);
				Thread.sleep(1000);
				
			}
		}catch(InterruptedException e){System.out.println("Main thread is interrupted");
		}
		System.out.println("Main is exiting");
	}
	public static void main(String args[]){
		Thread s=new Thread();
		s.start();
		
		System.out.println(s.isAlive());
		NewThread ob1=new NewThread("One");
		NewThread ob2=new NewThread("Two");
		System.out.println("Thread One is alive"+ob1.t.isAlive());
		System.out.println("Thread Two is alive"+ob2.t.isAlive());
	
		try{System.out.println("Waiting for threads to finish.");
		ob1.t.join();
		ob2.t.join();
		
		} catch (InterruptedException e) {
		System.out.println("Main thread Interrupted");
		}
		System.out.println("Thread One is alive: "
		+ ob1.t.isAlive());
		System.out.println("Thread Two is alive: "
		+ ob2.t.isAlive());
		System.out.println("Thread main is alive "+s.isAlive());
	}
}