package com.java.generics;

class GenOverride1<T> {
  T ob; // declare an object of type T  
    
  // Pass the constructor a reference to   
  // an object of type T.  
  GenOverride1(T o) {  
    ob = o;  
  }  
  
  // Return ob.  
  T getob() {  
    System.out.print("Gen's getob(): " ); 
    return ob;  
  }  
}  
 
// A subclass of Gen that overrides getob(). 
class GenOverride<T> extends GenOverride1<T> { 
 
  GenOverride(T o) { 
    super(o); 
  } 
   
  // Override getob(). 
  T getob() {  
    System.out.print("Gen2's getob(): "); 
    return ob;  
  }  
} 
  
// Demonstrate generic method override. 
public class OverrideGenericMethod {  
  public static void main(String args[]) {  
    
    // Create a Gen object for Integers. 
	  GenOverride1<Integer> iOb = new GenOverride1<Integer>(88); 
 
    // Create a Gen2 object for Integers. 
    GenOverride<Integer> iOb2 = new GenOverride<Integer>(99);  
   
    // Create a Gen2 object for Strings. 
    GenOverride<String> strOb2 = new GenOverride<String>("Generics Test");  
 
    System.out.println(iOb.getob()); 
    System.out.println(iOb2.getob()); 
    System.out.println(strOb2.getob()); 
  }  
}

