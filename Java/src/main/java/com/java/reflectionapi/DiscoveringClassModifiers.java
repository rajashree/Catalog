package com.java.reflectionapi;

import java.lang.reflect.*;
import java.awt.*;

public class DiscoveringClassModifiers{
	
	public static void main(String[] args){
		String s = new String();
		printModifiers(s);
	}
	
	public static void printModifiers(Object obj){
		
		Class c =obj.getClass();
		int m = c.getModifiers();
		System.out.println("obtaining the modifiers of the String class using c.getModifiers() ");
		if(Modifier.isPublic(m)){
			System.out.println("public");
		}
		if(Modifier.isAbstract(m)){
			System.out.println("abstract");
		}
		if(Modifier.isFinal(m)){
			System.out.println("final");
		}
		
	}
}