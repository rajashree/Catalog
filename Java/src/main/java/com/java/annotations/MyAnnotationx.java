/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.java.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author : Rajashree Meganathan
 * @date : 11/21/12
 */ // A simple annotation type.
@Retention(RetentionPolicy.RUNTIME)
@interface MyAnnotationx {
  String stringValue();

  int intValue();
}
