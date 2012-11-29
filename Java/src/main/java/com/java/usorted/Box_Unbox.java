package com.java.usorted;

public class Box_Unbox {
  public static void main(String args[]) {
    Boolean booleanObject = true;

    if (booleanObject){
      System.out.println("b is true");
    }
    
    Character ch = 'x'; // box a char
    char ch2 = ch; // unbox a char

    System.out.println("ch2 is " + ch2);
  }
}