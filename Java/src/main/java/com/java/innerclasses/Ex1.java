//Static Nested, Inner[Non-static Nested]class and local inner class
package com.java.innerclasses;

class Outer {
	int x=10;
	static int z=30;
	static class Inner1{
		int y=20;
		int disp(){
			System.out.println("value of static var z from outer class in static inner class "+ z);
			return y;
		}
	}
	class Inner2{
		int a=40;
		void display(){
			System.out.println("value of x from outer class in non-static inner class "+x);
		}
		void list(){
			System.out.println("value of a in non-static inner class "+a);
		}
	}
	void disp_out(){
		Inner2 in= new Inner2();
		in.display();
		in.list();
		//System.out.println("value of a "+a);   --- cannot access inner class members
	}
	void display(){
		System.out.println("Multiple inner class for single outer class:");
		for(int i=0;i<10;i++){
			class Inner3{
				void disp(){
					System.out.println("value of x from outer class "+x);
				}
			}
			Inner3 in=new Inner3();
			in.disp();
		}
	}
}

public class Ex1{
	public static void main(String arg[]){
		Outer.Inner1 inn1= new Outer.Inner1();
		System.out.println("Static inner class with return value y "+inn1.disp() );
		Outer out=new Outer();
		out.disp_out();
		out.display();
	}
}