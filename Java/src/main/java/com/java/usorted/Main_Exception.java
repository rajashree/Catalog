package com.java.usorted;

public class Main_Exception {
  public static void main(String[] args) throws Exception {
    try {
      throw new Exception();
    }catch (Exception e) {
      System.err.println("Caught in main()");
    }
  }
}