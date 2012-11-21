package com.java.annotations;

import java.lang.reflect.Method;
/*There are three annotation types in Java 5: Deprecated, 
 * Override, and Suppress Warnings.
 There are four other annotation types that are part of the 
 java.lang.annotation package: Documented, Inherited, Retention, and Target. 
 Meta annotations are annotations that are applied to annotations.
 There are four meta-annotation types: Documented, Inherited, Retention, and Target.*/

class Annotation {
  @MyAnno(str = "test")
  public static void myMeth() {
	  Annotation ob = new Annotation();

    try {
      Class<?> c = ob.getClass();
      Method m = c.getMethod("myMeth");
      MyAnno anno = m.getAnnotation(MyAnno.class);

      System.out.println(anno.str() + " " + anno.val());
    } catch (NoSuchMethodException exc) {
      System.out.println("Method Not Found.");
    }
  }

  public static void main(String args[]) {
    myMeth();
  }
}