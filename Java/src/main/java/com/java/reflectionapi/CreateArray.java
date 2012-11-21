package com.java.reflectionapi;
import java.lang.reflect.*;
public class CreateArray {
public static void main(String argsp[]){
	int[] originalArray = {50,60};
	int[] biggerArray = (int[]) doubleArray(originalArray);
	System.out.println("originalArray : ");
	for(int k=0;k<Array.getLength(originalArray);k++){
			System.out.println(originalArray[k]);
	}
	System.out.println("BiggerArray : ");
	for(int k1=0;k1<Array.getLength(biggerArray);k1++){
		System.out.println(biggerArray[k1]);
		
	}
	
}
  
   public static Object doubleArray(Object source) {
	   int sourceLength = Array.getLength(source);
	   Class arrayClass = source.getClass();
	   Class componentClass = arrayClass.getComponentType();
	   Object result = Array.newInstance(componentClass, sourceLength*3);
	  System.arraycopy(source,0,result,0,sourceLength);
	return result;
   }
}