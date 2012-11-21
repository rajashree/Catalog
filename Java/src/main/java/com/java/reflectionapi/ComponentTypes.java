package com.java.reflectionapi;
import java.awt.*;

public class ComponentTypes {
	
	public static void main(String args[]){
		int[] ints = new int[2];
		Button[] buttons = new Button[6];
		String[][] twoDim = new String[5][6];
		
		printComponentType(ints);
		printComponentType(buttons);
		printComponentType(twoDim);
		
	}
	
	public static void printComponentType(Object obj){
		Class arrayClass = obj.getClass();
		String arrayName = arrayClass.getName();
		Class componentClass = arrayClass.getComponentType();
		String componentName = componentClass.getName();
		
		System.out.println("Array : "+arrayName + ", Component : "+ componentName);
	}
}

