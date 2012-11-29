package com.java.usorted;

import java.util.*;

public class SortArrayList
{    
     public static void main(String s[])
     {
        // The input is a String array. Make that an ArrayList:
        List inputList = new ArrayList(Arrays.asList("3","23","0","323","1"));
       
        List l = sort(inputList);
         
        System.out.println("\nStrings sorted List ...");
        for(int i = l.size()-1; i >0; i--)
            System.out.println((String)l.get(i));
    }
     
    public static List sort(List list) {
          Collections.sort(list, new Comparator() {

            public int compare(Object o1, Object o2) {
                 String s1 = (String)o1;
                 String s2 = (String)o2;

                 String integer1[] = s1.split("[^0-9]");      // <<<<<  changed
                 String integer2[] = s2.split("[^0-9]");      // <<<<<  changed
                 String chars1[] = s1.split("[0-9]+");         // <<<<<  changed
                 String chars2[] = s2.split("[0-9]+");         // <<<<<  changed

                 Integer i1 = new Integer( Integer.parseInt(integer1[0]) );
                 Integer i2 = new Integer( Integer.parseInt(integer2[0]) );

                 if (i1.equals(i2))
                    return chars1[1].compareTo(chars2[1]);
                  else
                     return i1.compareTo(i2);
            }
        });
        return list;
    }
}