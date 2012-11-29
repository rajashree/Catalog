package com.java.usorted;

import java.util.*;

public class ArrayEx {
	
	public static void main(String areg[]){
		
		int a[]= new int[10];
		for(int i=0; i<10;i++){
			a[i]= -3*i;
		}
		System.out.println("The content of original array:");
		display(a);
		
		Arrays.fill(a,3,7,-5);
		System.out.println("The content after filling");
		display(a);
		
		Arrays.sort(a);
		System.out.println("The content after sorting");
		display(a);
					
		int key=0;
		int index=Arrays.binarySearch(a, key);
		System.out.println("the value "+key+" is present at "+index);
		
		
	}
	static void display(int a[]){
		for(int i=0;i<a.length;i++){
			System.out.println("\t "+ a[i]);
		}
	}

}
