package com.java.reflectionapi;

import java.lang.reflect.*;
import java.awt.*;

public class GetClassName1 {

	public static void main(String[] args) throws ClassNotFoundException{
		
		Button b = new Button();
		printName(b);
	}
	static void printName(Object o) throws ClassNotFoundException{
		
		//Obtaining the class name from object
		
		Class c1 = o.getClass();
		System.out.println("The class Name obtained using getClass() "+c1.toString());
		String s1 = c1.getName();
		System.out.println("The class name obtained using getClass(), getName() for Button object is "+s1);
		
		//Obtaining the supler class name from the object
		
		TextField t = new TextField(); 
		Class c2 = t.getClass(); 
		Class s2 = c2.getSuperclass();
		System.out.println("The super class name obtained using getClass(), getSuperclass() for TextField object is "+s2);
		
		//Obtaining an object using .class() method
		Class c3 = Button.class;
		System.out.println("Obtaining the class object using the Class Name "+c3.toString());
		
		//Obtaining an object for a class .. which is available only at runtime..UnknownClassName
		String argument = "java.awt.Button";
		Class c4 = Class.forName(argument);
		System.out.println("Class object obtained.. which is available at runtime using Class.forName() "+c4.toString());
		
	}
}