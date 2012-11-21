package com.java.reflectionapi;
import java.lang.reflect.*;
public class GetSetArray {
	public static void main(String args[]){
		int[] sourceInts = {12,11};
		int[] destInts = new int[2];
		copyArray(sourceInts,destInts);
		String[] sourceStrgs = {"Devi","Jumboo","Raje"};
		String[] destStrgs = new String[3];
		copyArray(sourceStrgs,destStrgs);
		
	}
public static void copyArray(Object source, Object dest){

	for(int i=0;i<Array.getLength(source);i++){
		Array.set(dest,i,Array.get(source,i));
		System.out.println(Array.get(dest,i));
		
	}
}
}