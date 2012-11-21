package com.java.reflectionapi;
import java.lang.reflect.*;
import java.awt.*;
public class CreateInstance_WithArguments {
 
	public static Object createObject(Constructor constructor, Object[] arguments){
		System.out.println("Constructor : "+constructor.toString());
		Object object = null;
		try {
			object = constructor.newInstance(arguments);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Object : "+object.toString());
		return object;
		
	}
	public static void main(String[] args){
		Rectangle rectangle;
		Class rectangleDefinition = null;
		Class[] intArgClass = new Class[] {int.class,int.class};
		Integer height = new Integer(12);
		Integer width = new Integer(21);
		Object[] intArgs = new Object[] {height,width};
		Constructor intArgsConstructor = null;
		try {
			rectangleDefinition = Class.forName("java.awt.Rectangle");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			intArgsConstructor = rectangleDefinition.getConstructor(intArgClass);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		rectangle = (Rectangle) createObject(intArgsConstructor, intArgs);
		
	}
}