package com.java.generics;

class Gen3<T> {
	T ob;  

	Gen3(T o) {   
		ob = o;   
	}   

	// Return ob.   
	T getob() {   
		return ob;   
	}   
}   

//A subclass of Gen3.  
class Gen4<T> extends Gen3<T> {  
	Gen4(T o) {  
		super(o);  
	}  
}  

// Demonstrate runtime type ID implications of generic class hierarchy.  
public class InstanceOfOperator {   
	public static void main(String args[]) {   

		// Create a Gen object for Integers.  
		Gen3<Integer> iOb = new Gen3<Integer>(88);  

		// Create a Gen2 object for Integers.  
		Gen4<Integer> iOb2 = new Gen4<Integer>(99);   

		// Create a Gen2 object for Strings.  
		Gen4<String> strOb2 = new Gen4<String>("Generics Test");   

		// See if iOb2 is some form of Gen2. 
		if(iOb2 instanceof Gen4<?>)   
			System.out.println("iOb2 is instance of Gen2");  

		// See if iOb2 is some form of Gen. 
		if(iOb2 instanceof Gen3<?>)   
			System.out.println("iOb2 is instance of Gen");  

		System.out.println();  

		// See if strOb2 is a Gen2. 
		if(strOb2 instanceof Gen4<?>)   
			System.out.println("strOb is instance of Gen2");  

		// See if strOb2 is a Gen. 
		if(strOb2 instanceof Gen3<?>)   
			System.out.println("strOb is instance of Gen");  

		System.out.println();  

		// See if iOb is an instance of Gen2, which its not. 
		if(iOb instanceof Gen4<?>)   
			System.out.println("iOb is instance of Gen2");  

		// See if iOb is an instance of Gen, which it is. 
		if(iOb instanceof Gen3<?>)   
			System.out.println("iOb is instance of Gen");  

		// The following can't be compiled because  
		// generic type info does not exist at runtime. 
//		if(iOb2 instanceof Gen4<Integer>)   
//		System.out.println("iOb2 is instance of Gen4<Integer>");  
	}   
}