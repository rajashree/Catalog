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
 */
@Retention(RetentionPolicy.RUNTIME)
@interface MyAnno {
  String str();
  int val() default 9000;
}
