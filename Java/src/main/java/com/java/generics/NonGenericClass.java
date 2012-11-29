package com.java.generics;

class NonGen {
  Object ob; 
    
  NonGen(Object o) {  
    ob = o;  
  }  
  
  Object getob() {  
    return ob;  
  }  
  void showType() {  
    System.out.println("Type of ob is " +  
                       ob.getClass().getName());  
  }  
}  
  
 
public class NonGenericClass {  
  public static void main(String args[]) {  
    NonGen integerObject;   
    integerObject = new NonGen(88);  
  
    integerObject.showType(); 
 
    int v = (Integer) integerObject.getob();  
    System.out.println("value: " + v);  
  
 
    NonGen strOb = new NonGen("Non-Generics Test");  
    strOb.showType(); 
 
    String str = (String) strOb.getob();  
    System.out.println("value: " + str);  
 
    integerObject = strOb; 
    v = (Integer) integerObject.getob(); 
  }  
}
