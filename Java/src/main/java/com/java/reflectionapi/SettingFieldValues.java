package com.java.reflectionapi;
import java.lang.reflect.*;
import java.awt.*;
public class SettingFieldValues {
	public static void main(String args[]){
		Rectangle r = new Rectangle(100,20);
		System.out.println("original : "+r.toString());
		modifyWidth(r,new Integer(300));
		System.out.println("modified : "+r.toString());
	}
	public static void modifyWidth(Rectangle r, Integer widthParam){
		Field widthField = null;
		Integer widthValue;
		Class c = r.getClass();
		try {
			widthField = c.getField("width");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			widthField.get(r);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("B4 modification "+r.toString());
		try {
			widthField.set(r,widthParam);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("After modification "+r.toString());
	}

}