package com.java.generics;

class GenericType<T> {
  T ob; // declare an object of type T  
    
  // Pass the constructor a reference to   
  // an object of type T.  
  GenericType(T o) {  
    ob = o;  
  }  
  
  // Return ob.  
  T getob() {  
    return ob;  
  }  
}  
  
// Demonstrate raw type. 
public class RawGenericType {  
  public static void main(String args[]) {  
 
    // Create a Gen object for Integers. 
    GenericType<Integer> iOb = new GenericType<Integer>(88);  
   
    // Create a Gen object for Strings. 
    GenericType<String> strOb = new GenericType<String>("Generics Test");  
  
    // Create a raw-type Gen object and give it 
    // a Double value. 
    GenericType raw = new GenericType(new Double(98.6)); 
 
    // Cast here is necessary because type is unknown. 
    double d = (Double) raw.getob(); 
    System.out.println("value: " + d); 
 
    // The use of a raw type can lead to runtime. 
    // exceptions.  Here are some examples. 
 
    // The following cast causes a runtime error! 
//    int i = (Integer) raw.getob(); // runtime error 
 
    // This assigment overrides type safety. 
    strOb = raw; // OK, but potentially wrong 
//    String str = strOb.getob(); // runtime error  
     
    // This assignment also overrides type safety. 
    raw = iOb; // OK, but potentially wrong 
//    d = (Double) raw.getob(); // runtime error 
  }  
}