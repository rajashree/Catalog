package com.java.reflectionapi;
import java.lang.reflect.*;
import java.awt.*;
public class IdentifyingClassFields {

	public static void main(String args[]){
		GridBagConstraints g = new GridBagConstraints();
		printFieldNames(g);
	}
	public static void printFieldNames(Object o){
	
		Class c = o.getClass();
		Field[] publicFields = c.getFields();
		for(int i=0;i<publicFields.length;i++){
			String fieldName = publicFields[i].getName();
			Class typeClass = publicFields[i].getType();
			String fieldType = typeClass.getName();
			System.out.println("Field Name "+fieldName+" & FieldType "+fieldType);
		}
		
	}
	
}