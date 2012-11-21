package com.java.annotations;

import java.util.Date;
 
public class Annotation_SuppressWarnings {
   /*
   The following are valid parameters to @SuppressWarnings:

   1. unchecked. Give more detail for unchecked conversion.
   2. path. Warn about nonexistent path (classpath, sourcepath, etc) directories.
   3. serial. Warn about missing serialVersionUID definitions on serializable classes.
   4. finally. Warn about finally clauses that cannot complete normally.
   5. fallthrough. Check switch blocks for fall-through cases.
   switch (i) {
      case 1:
          System.out.println("1");
          break;
      case 2:
          System.out.println ("2");
          // falling through
      case 3:
          System.out.println ("3");
      }
    
    */
	@SuppressWarnings(value={"deprecation"})
    public static void main(String[] args) {
        Date date = new Date(2009, 9, 30);
 
        System.out.println("date = " + date);
    }
}