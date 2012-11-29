package com.java.generics;

class Gen1<T> {
	T ob; // declare an object of type T
	Gen1(T o) {
		ob = o;
	}
	T getob() {
		return ob;
	}
	void showType() {
		System.out.println("Type of T is " + ob.getClass().getName());
	}
}

public class GenericClass {
	public static void main(String args[]) {
		Gen1<Integer> iOb;
		iOb = new Gen1<Integer>(88);
		iOb.showType();
		int v = iOb.getob();
		System.out.println("value: " + v);
		Gen1<String> strOb = new Gen1<String>("Generics Test");
		strOb.showType();
		String str = strOb.getob();
		System.out.println("value: " + str);
	}
}