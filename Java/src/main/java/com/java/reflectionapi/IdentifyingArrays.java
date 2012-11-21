/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.java.reflectionapi;

import java.awt.*;
import java.lang.reflect.Field;

/**
 * @author : Rajashree Meganathan
 * @date : 11/21/12
 */
public class IdentifyingArrays {
public static void main(String args[]){
	System.out.println("Identifying Arrays");
	KeyPad obj = new KeyPad();
	printArrayNames(obj);
}

public static void printArrayNames(Object target){
	Class targetClass = target.getClass();
	Field[] publicFields = targetClass.getFields();
	for(int i=0;i<publicFields.length;i++){
		String fieldName = publicFields[i].getName();
		Class typeClass = publicFields[i].getType();
		String fieldType = typeClass.getName();
		if(typeClass.isArray()){
			System.out.println("Name : "+fieldName +", Type : "+fieldType);
		}

	}
	}


}

class KeyPad{
	public boolean alive;
	public Button power;
	public Button[] letters;
	public int[] codes;
	public TextField[] rowsl;
	public boolean[] states;
}