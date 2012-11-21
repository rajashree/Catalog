package com.creational.singleton.ex2;

public class SinglePrinter {
  static public void main(String argv[]) {
    Printer pr1, pr2;
    //open one printer--this should always work
    System.out.println("Opening one printer");
    try {
      pr1 = new Printer();
    } catch (SingletonException e) {
      System.out.println(e.getMessage());
    }
    //try to open another printer --should fail
    System.out.println("Opening two printers");
    try {
      pr2 = new Printer();
    } catch (SingletonException e) {
      System.out.println(e.getMessage());
    }
  }
}           