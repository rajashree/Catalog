package com.structural.facade;

public class Class2 {

	public int doAnotherThing(Class1 class1, int x) {
		return 2 * class1.doSomethingComplicated(x);
	}
}
