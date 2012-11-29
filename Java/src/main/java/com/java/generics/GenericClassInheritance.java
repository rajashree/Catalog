package com.java.generics;

// Here, T is bound by Object by default.
class GenClass<T> {  
  T ob; // here, T will be replaced by Object 
    
  GenClass(T o) {  
    ob = o;  
  }  
  
  // Return ob.  
  T getob() {  
    return ob;  
  }  
}  
 
// Here, T is bound by String. 
class GenStr<T extends String> { 
  T str; // here, T will be replaced by String 
 
  GenStr(T o) {  
    str = o;  
  }  
 
  T getstr() { return str; } 
}

public class GenericClassInheritance {  
  public static void main(String args[]) {  
    GenClass<Integer> iOb = new GenClass<Integer>(99);  
    GenClass<Float> fOb = new GenClass<Float>(102.2F); 
 
    System.out.println(iOb.getClass().getName()); 
    System.out.println(fOb.getClass().getName()); 
  } 
}