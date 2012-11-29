package com.java.usorted;

import java.util.*;

import com.java.usorted.Address;

public class Mail{

	public static void main(String args[]){
	
	LinkedList lt= new LinkedList();
	lt.add(new Address("devashree",134,"3rd cross; N T Sandra; B'lore-75"));
	lt.add(new Address("Rajashree",343,"4th main JP Nagar, B'lore-75"));
	Iterator it=lt.iterator();
	System.out.println("The Mail List");
	while(it.hasNext()){
		System.out.println(it.next());
	}
		
		

	}
}
