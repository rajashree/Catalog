package com.java.usorted;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class HashmapEx {
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
		
		ArrayList<String> list = new ArrayList<String>();
		list.add(ex.get("name"));
		list.add(ex2.get("name"));
		list.add(ex3.get("name"));
		
		Collections.sort(list);
		System.out.println(list);
		
		/*boolean blnExists = ex3.containsValue("Prateek");
		
		System.out.println("3 exists in HashMap ? : " + blnExists);*/
		
		HashMap hMap = new HashMap();
		
		hMap.put("1","One");
		
		hMap.put("2","Two");
		
		hMap.put("3","Three");
		
		boolean blnExists = hMap.containsKey("2");
		
		System.out.println("3 exists in HashMap ? : " + blnExists);
		
	}

}
