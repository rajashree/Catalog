package com.java.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@MyAnnotation1
public class MarkerAnnotation {
  // Annotate a method.
  @MyAnnotation1()
  public static void myMethod() {
  }

  public static void main(String[] arg) {
    try {
    	MarkerAnnotation ob = new MarkerAnnotation();
    	  
      Method m = ob.getClass( ).getMethod("myMethod");
      if (m.isAnnotationPresent(MyAnnotation1.class))
	        System.out.println("MyMarker is present.");
      Annotation[] annos = m.getAnnotations();

      System.out.println("All annotations for myMeth:");
      for(Annotation a : annos)
      System.out.println(a);

    } catch (Exception exc) {
    }
  }
}