package com.java.annotations;

import java.util.ArrayList;
import java.util.Iterator;

public class Annotation_Unchecked {

	//else add Generics like : ArrayList<String>
  @SuppressWarnings("unchecked")
  public static void main(String[] args) {
    ArrayList data = new ArrayList();
    data.add("hello");
    data.add("world");

    Iterator it = data.iterator();
    while (it.hasNext()) {
      System.out.println(it.next());
    }
  }
}