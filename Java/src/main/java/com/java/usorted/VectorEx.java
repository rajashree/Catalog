package com.java.usorted;

import java.util.*;

public class VectorEx {

	public static void main(String a[]){
		
		//Vector v= new Vector();
		Vector v=new Vector(4,10);
		int size= v.size();
		System.out.println("Size of the vector is "+size);
		System.out.println("The vector capacity is "+ v.capacity());
		v.add(new Integer(9));
		v.add("dev");
		v.add("raj");
		v.add("mega");
		v.add("jaya");
		v.add(new Double(78.9));
		System.out.println("By iterator:\t");
		Iterator it= v.iterator();
		while(it.hasNext()){
			System.out.println(it.next());
		}
		System.out.println("Size after addition is " +v.size());
		System.out.println("The vector capactity is "+v.capacity());
		v.trimToSize();
		System.out.println("The vector capacity after trimming "+v.capacity());
		System.out.println("By Enumeratoin:\t");
		Enumeration venum=v.elements();
		while(venum.hasMoreElements()){
			System.out.println(venum.nextElement());
		}
		
	}
}
