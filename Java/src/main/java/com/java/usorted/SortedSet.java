package com.java.usorted;

import java.util.Iterator;
import java.util.TreeSet;

public class SortedSet {
	public static void main(String a[]){
	  // Create the sorted set
    TreeSet set = new TreeSet();
    
    // Add elements to the set
    String n ="1";
    
    set.add(423);
    set.add(1234);
    set.add(Integer.parseInt(n));
    set.add(453);
    set.add(Integer.parseInt(n));
    Object arr[] = set.toArray();
    System.out.println("sorted list:::Descending");
    for(int i =arr.length-1 ;i>=0;i--)System.out.println(arr[i].toString());
    // Iterating over the elements in the set
    Iterator it = set.iterator();
    System.out.println("sorted list:::Ascending");
    while (it.hasNext()) {
        // Get element
        //Object element = it.next();
        System.out.println(it.next().toString());
    }
	
	}
}
