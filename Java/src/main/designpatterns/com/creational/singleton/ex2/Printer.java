/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.creational.singleton.ex2;

/**
 * @author : Rajashree Meganathan
 * @date : 11/20/12
 */ /*
The Design Patterns Java Companion

Copyright (C) 1998, by James W. Cooper

IBM Thomas J. Watson Research Center

*/
class Printer {
  //this is a prototype for a printer-spooler class
  //such that only one instance can ever exist
  static boolean instance_flag = false; //true if 1 instance

  public Printer() throws SingletonException {
    if (instance_flag)
      throw new SingletonException("Only one printer allowed");
    else
      instance_flag = true; //set flag for 1 instance
    System.out.println("printer opened");
  }

  public void finalize() {
    instance_flag = false;
  }
}
