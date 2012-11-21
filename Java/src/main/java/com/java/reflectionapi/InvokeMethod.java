package com.java.reflectionapi;
import java.lang.reflect.*;
public class InvokeMethod {
	public static void main(String args[]){
		String firstWord = "First";
		String secondWord = "Second";
		String bothWords = append(firstWord,secondWord);
		System.out.println("bothWords after appending "+bothWords);
	}
	public static String append(String firstWord, String secondWord){
		String result = null;
		
		Class c =  String.class;
		Class[] parameterTypes =new Class[] {String.class};
		Method concatMethod = null;
		Object[] arguments = new Object[] {secondWord};
		try {
			concatMethod = c.getMethod("concat",parameterTypes);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			result = (String)concatMethod.invoke(firstWord, arguments);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	
	} 
}