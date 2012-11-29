package com.java.usorted;

import java.util.*;

public class StringTokenizer1{

  public static void main(String[] arg){
    String text = "to be or not to be, that is the question.";
    String[] words = text.split("[, .]", 0); // Delimiters are comma, space, or period
    
    for(String s: words){
      System.out.println(s);
    }
    
    for (String x : parseList("a,b,c,d,e",",")){
    	System.out.println(x);
    }
  }
  @SuppressWarnings ("unchecked")
  public static String[] parseList(String list, String delim) {
      List result = new ArrayList();
      StringTokenizer tokenizer = new StringTokenizer(list, delim);
      while (tokenizer.hasMoreTokens()) {
          result.add(tokenizer.nextToken());
      }
      return (String[]) result.toArray(new String[0]);
  }

}

