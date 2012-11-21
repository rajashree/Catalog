package com.java.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;


@MyAnnotation(102)
public class SingleMemberAnnotation {
  // Annotate a method.
  @MyAnnotation(101)
  public static void myMethod() {
  }

  public static void main(String[] arg) {
    try {
    	SingleMemberAnnotation ob = new SingleMemberAnnotation();

      Method m = ob.getClass( ).getMethod("myMethod");
      Annotation[] annos = m.getAnnotations();

      System.out.println("All annotations for myMeth:");
      for(Annotation a : annos)
      System.out.println(a);

    } catch (Exception exc) {
    }
  }
}