package com.java.usorted;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class HashMapSort {
	public static void main(String args[]){
		Map<String,String> ex = new HashMap<String,String>();
		ex.put("name", "Karthik");
		ex.put("position", "employee");
		Map<String,String> ex2 = new HashMap<String,String>();
		ex2.put("name","Shalini");
		ex2.put("position", "manager");
		Map<String,String> ex3 = new HashMap<String,String>();
		ex3.put("name","Prateek");
		ex3.put("position", "CEO");
		Map<String,String> ex4 = new HashMap<String,String>();
		ex4.put("name","Devashree");
		ex4.put("position", "TL");
		
		ArrayList<Map<String,String>> list = new ArrayList<Map<String,String>>();
		list.add(ex);
		list.add(ex2);
		list.add(ex3);
		list.add(ex4);
		
		Comparator<Object> comp = new Comparator<Object>() {
			public int compare(Object ob1, Object ob2) {
				Map<String, String> m1 = (Map<String, String> ) ob1;
				Map<String, String>  m2 = (Map<String, String> ) ob2;
				return ((String) m1.get("name")).compareTo((String)m2.get("name"));
             }

			
        };
		Collections.sort(list, comp);
        
		System.out.println(list);
		
	}

}
