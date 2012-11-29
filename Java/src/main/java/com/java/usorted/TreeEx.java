package com.java.usorted;

import java.util.*;

public class TreeEx {

	public static void main(String arg[]){
		
		TreeMap tm=new TreeMap();
		
		tm.put("Dev", new Double(1700.0));
		tm.put("Raj", new Double(200.0));
		tm.put("Jay", new Double(600.0));
		tm.put("Rahul",new Double(200.0));
		tm.put("John",new Double(500.60));
		
		Set set= tm.entrySet();
		
		Iterator it=set.iterator();
		
		while(it.hasNext()){
			Map.Entry m=(Map.Entry) it.next();
			System.out.print(m.getKey()+": ");
			System.out.println(m.getValue());
		}
			
	}
}
