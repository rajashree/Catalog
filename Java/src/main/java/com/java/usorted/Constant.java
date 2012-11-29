package com.java.usorted;

public class Constant {
  public static final String CONST_AVAILABLE_OUTSIDE = "Available outside the Class";
  static final String CONST_AVAILABLE_INSIDE = "Available inside this Class";
  public static void main(String[] arg) {
           // Constant values
    final double MM_PER_INCH = 25.4;      // that cannot be changed

    System.out.println(CONST_AVAILABLE_INSIDE);
    System.out.println(MM_PER_INCH);
    System.out.println(CONST_AVAILABLE_OUTSIDE);
  }

}
