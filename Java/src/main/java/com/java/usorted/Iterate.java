package com.java.usorted;

import java.util.*;

public class Iterate {
	/**
	 * @param are
	 */
	public static void main(String are[]){
		
		ArrayList al=new ArrayList();
		
		al.add("a");
		al.add("f");
		al.add("d");
		al.add("b");
		for(int i=0;i<al.size();i++){
			System.out.println("DEV::::::::"+al.get(i));
		}
		Iterator it=al.iterator();
		System.out.println("the content of the list is --->");
		System.out.println(al);
		while(it.hasNext()){
			System.out.println(it.next());
		}
		
		ListIterator lit=al.listIterator();
		System.out.println("the content of the list by list iterator---->");
		System.out.println(lit);
		while(lit.hasNext()){
			System.out.println("\nnex---->"+lit.next());
			
		}
		while(lit.hasPrevious()){
			System.out.println("\nprev---->"+lit.previous());
		}
			
	}

}
