package com.java.reflectionapi;
import java.lang.reflect.*;
import java.awt.*;

public class GettingFieldValues {
	
public static void main(String args[]){
	Rectangle r = new Rectangle(100,325);
	printHeight(r);
	
}
public static void printHeight(Rectangle rect){
	Field heightField = null;
	Integer heightValue = null;
	Field widthField = null;
	Integer widthValue = null;
	Class c = rect.getClass();
	try {
		
		heightField = c.getField("height");
		widthField = c.getField("width");
	} catch (SecurityException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (NoSuchFieldException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		heightValue = (Integer)heightField.get(rect);
		widthValue = (Integer)widthField.get(rect); 
			
	} catch (IllegalArgumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalAccessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.out.println("Height : "+heightValue.toString());
	System.out.println("Width: "+widthValue.toString());

}
}