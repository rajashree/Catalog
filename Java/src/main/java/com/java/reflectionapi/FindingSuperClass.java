package com.java.reflectionapi;
import java.lang.reflect.*;
import java.awt.*;

public class FindingSuperClass {

	public static void main(String[] args){
		Button b = new Button();
		printSuperclasses(b);
	}
	public static void printSuperclasses(Object o){
		Class subclass = o.getClass();
		Class superclass = subclass.getSuperclass();
		while(superclass != null){
			String className = superclass.getName();
			System.out.println("Superclass Name = "+className+"  <==  Subclass Name"+subclass.getName());
			subclass = superclass;
			superclass=subclass.getSuperclass();
		}
	}
}