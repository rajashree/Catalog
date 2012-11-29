package com.java.usorted;

import java.util.ArrayList;


public class arraylist {
	
	/**
	 * @param args
	 */
	/**
	 * @param args
	 */
	/**
	 * @param args
	 */
	/**
	 * @param args
	 */
	/**
	 * @param args
	 */
	public static void main(String args[]){
		
		ArrayList a1= new ArrayList();
		ArrayList tagnames= new ArrayList();
		ArrayList tagcounts= new ArrayList();
		a1.add("dev");
		a1.add("Raj");
		a1.add("Jam");
		tagnames.add("dev");
		tagnames.add("Raj");
		tagnames.add("Jam");
		tagcounts.add(1);
		tagcounts.add(2);
		tagcounts.add(3);
		a1.add(new Integer(3));
		System.out.println("the size of the arraylist before deletion is "+a1.size());
		System.out.println("the content is "+a1);
		a1.remove("Rahul");
		a1.remove(2);
		System.out.println("the size of the arraylist after deletion is "+a1.size());
		System.out.println("the content is "+a1);
		for(int xx=0;xx<tagnames.size();xx++){
			//System.out.println(":::::::::::"+tagnames.get(xx)+":::::::::::::"+tagcounts.get(xx));
			int count = 0;
			for(int kk=0;kk<tagnames.size();kk++){
			//System.out.println("-----"+tagnames.get(kk)+"----------"+tagcounts.get(kk));
			if(tagnames.get(xx).equals(tagnames.get(kk))){
				count+= (Integer)tagcounts.get(kk);
			}
			
		}System.out.println("**********"+tagnames.get(xx)+"----------"+count);
		}
	}

}
