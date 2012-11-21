package com.java.reflectionapi;

import java.lang.reflect.*;
import java.awt.*;
public class ConstructorParams {
public static void main(String args[]){
	Rectangle r = new Rectangle();
	showConstructors(r);
}


static void showConstructors(Object o) {
    Class c = o.getClass();
    Constructor[] theConstructors = c.getConstructors();
    for (int i = 0; i < theConstructors.length; i++) {
       System.out.print("( ");
       Class[] parameterTypes = 
          theConstructors[i].getParameterTypes();
       for (int k = 0; k < parameterTypes.length; k ++) {
          String parameterString = parameterTypes[k].getName();
          System.out.print(parameterString + " ");
          }
       
       System.out.println(")");
       }
 }
}
