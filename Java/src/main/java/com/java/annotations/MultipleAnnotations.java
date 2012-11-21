package com.java.annotations;

import java.lang.annotation.Annotation;

@What(description = "An annotation test class")
@MyAnnotationx(stringValue = "for class", intValue = 100)
public class MultipleAnnotations {
  // Annotate a method.
  @What(description = "An annotation test method")
  @MyAnnotationx(stringValue = "Annotation Example", intValue = 100)
  public static void myMethod(String str, int i) {
  }

  public static void main(String[] arg) {
    try {
    	MultipleAnnotations ob = new MultipleAnnotations();
      Annotation[] annos = ob.getClass().getAnnotations();
      
      System.out.println("All annotations for Meta2:");
      for(Annotation a : annos)
        System.out.println(a);
      
    } catch (Exception exc) {
    }
  }
}