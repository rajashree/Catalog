package com.structural.facade.ex1;

public class Class2 {

	public int doAnotherThing(Class1 class1, int x) {
		return 2 * class1.doSomethingComplicated(x);
	}
}
