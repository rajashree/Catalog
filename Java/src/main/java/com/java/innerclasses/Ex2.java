package com.java.innerclasses;

abstract class Base {
	  public Base(int i) {
	    System.out.println("Base constructor, i = " + i);
	  }
	
	  public abstract void f();
	  
	  public static void hello(){
		  System.out.println("HELOOOOOOOOOOOO");
	  }
	}

	
	public class Ex2 {

	  public static Base getBase(int i) {
	    return new Base(i) {
	      {
	        System.out.println("Inside instance initializer");
	      }

	      public void f() {
	        System.out.println("In anonymous f()");
	      }
	    };
	  }

	  public static void main(String[] args) {
		Base.hello();	//accessing the static method direclty without creating the object
		Base base = getBase(47);
		base.f();
	  }
	};