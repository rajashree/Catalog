package com.java.reflectionapi;

import java.lang.reflect.*;
import java.awt.*;
public class CreateInstance_NoArguments {
public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
	Rectangle r = (Rectangle) createObject("java.awt.Rectangle");
	System.out.println(r.toString());
	
}
public static Object createObject(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
	Object object = null;
	Class classDefinition = Class.forName(className);
	object = classDefinition.newInstance();
	return object;
}
}
