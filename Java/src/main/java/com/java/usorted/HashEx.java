package com.java.usorted;

import java.util.*;
import java.util.Map.Entry;

public class HashEx {
	public static void main(String arg[]){
		
		LinkedHashMap hm= new LinkedHashMap();
		
		hm.put("Dev", new Double(1700.0));
		hm.put("Raj", new Double(250.0));
		hm.put("Jay", new Double(600.0));
		hm.put("Rahul",new Double(200.0));
				
		int hashc=hm.hashCode();
		System.out.println("The hash code is:"+hashc);
	
		Set set = hm.entrySet();
		Set kset= hm.keySet();
		System.out.println("::::"+hm.size());
		Iterator it= hm.entrySet().iterator();
		Iterator kit=set.iterator();
			
		while(it.hasNext()&& kit.hasNext()){
			
			Entry me= (Entry) it.next();
			
			System.out.print( me.getKey()+": ");
			System.out.println(me.getValue());
			
			System.out.println("key value---->"+ kit.next());
			
		}
		
		Double bal=(Double) hm.get("Dev");
		System.out.println("balance for Dev is:"+ bal);
		
		hm.put("Dev",new Double(300)+bal);
		System.out.println("the changed value for Dev is:"+hm.get("Dev"));
		
		hm.put("John", new Double(500));
		
		Collection c=hm.values();
		
		Iterator cit=c.iterator();
		System.out.println("the hashmap value by using collection is ");
		while(cit.hasNext()){
			System.out.println(cit.next());
		}
		
		
		
	}

}
