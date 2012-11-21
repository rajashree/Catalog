/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.creational.singleton.ex2;

/**
 * @author : Rajashree Meganathan
 * @date : 11/20/12
 */
class SingletonException extends RuntimeException {
  //new exception type for singleton classes
  public SingletonException() {
    super();
  }

  public SingletonException(String s) {
    super(s);
  }
}
