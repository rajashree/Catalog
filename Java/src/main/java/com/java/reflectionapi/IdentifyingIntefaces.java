package com.java.reflectionapi;

import java.lang.reflect.*;
import java.io.*;

public class IdentifyingIntefaces {

	public static void main(String args[]) throws FileNotFoundException{
	RandomAccessFile r = new RandomAccessFile("C:\\Documents and Settings\\Rajashree\\MyWorkSpace\\JavaReflectionAPI\\com\\java\\reflectionapi\\myfile.txt","r");
	printInterfaceName(r);
	}

	static void printInterfaceName(Object o){
		System.out.println("obtaining the Interfaces implemented by the RandomAccessFile class");
		Class c = o.getClass();
		Class[] theInterfaces = c.getInterfaces();
		for(int i=0;i<theInterfaces.length;i++){
			String interfaceName = theInterfaces[i].getName();
			System.out.println("The interface name "+interfaceName);
		}

	}
}