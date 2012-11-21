package com.java.reflectionapi;

import java.lang.reflect.*;
import java.awt.*;

public class InvokingMethods {
public static void main(String args[]){
	Polygon p = new Polygon();
	
	showMethods(p);
}
public static void showMethods(Object o){
	Class c = o.getClass();
	Method[] theMethods = c.getMethods();
	for(int i=0;i<theMethods.length;i++){
		String methodString = theMethods[i].getName();
		System.out.println("Name : "+methodString);
		String returnString = theMethods[i].getReturnType().getName();
		System.out.println("Return type "+theMethods[i].getReturnType().getName());
		Class[] parameterTypes = theMethods[i].getParameterTypes();
		System.out.println("Parameter types : ");
		for (int k=0;k<parameterTypes.length;k++){
			String parameterString = parameterTypes[k].getName();
			System.out.println(" "+parameterString);
		}
		System.out.println();
	}
}
}