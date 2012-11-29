package com.java.usorted;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;


public class ListEx {
public static void main(String[] arg){
	Collection a = new ArrayList() ;
	a.add("test1");
	a.add("test2");
	a.add("test3");
	a.add("test4");
	for(Object e : a){
		System.out.println("the values: "+e.toString());
	}
	System.out.println("thru iterator");
	Iterator it = a.iterator();
	while(it.hasNext()){
		System.out.println("ele value : "+it.next());
	}
	
}
}
