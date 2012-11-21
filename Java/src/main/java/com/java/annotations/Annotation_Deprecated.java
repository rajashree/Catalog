/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.java.annotations;

/**
 * @author : Rajashree Meganathan
 * @date : 11/21/12
 */
public class Annotation_Deprecated {
  public static void main(String[] args) {
    DeprecatedTest test = new DeprecatedTest();
    test.serve();
  }
}

class DeprecatedTest {
  @Deprecated
  public void serve() {

  }

}