/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.creational.singleton.ex2;

/**
 * @author : Rajashree Meganathan
 * @date : 11/20/12
 */ //Static method implementation approach with private constructor
public class InstanceSpooler {
  static public void main(String argv[]) {
    Spooler pr1, pr2;
    //open one printer--this should always work
    System.out.println("Opening one spooler");
    pr1 = Spooler.Instance();
    if (pr1 != null)
      System.out.println("got 1 spooler");
    //try to open another printer --should fail
    System.out.println("Opening two spoolers");

    pr2 = Spooler.Instance();
    if (pr2 == null)
      System.out.println("no instance available");
    //fails because constructor is privatized
    //iSpooler pr3 = new iSpooler();
  }
}

class Spooler {
  //this is a prototype for a printer-spooler class
  //such that only one instance can ever exist
  static boolean instance_flag = false; //true if 1 instance

  /** A private Constructor prevents any other class from instantiating. */
  private Spooler() {
  }

  /** Static 'instance' method */
   static public com.creational.singleton.ex2.Spooler Instance() {
    if (!instance_flag) {
      instance_flag = true;
      return new com.creational.singleton.ex2.Spooler();
    } else
      return null;
  }

  public void finalize() {
    instance_flag = false;
  }
}