package com.structural.bridge.ex1;

public class BigEngine implements Engine {

	int horsepower;

	public BigEngine() {
		horsepower = 350;
	}

	public int go() {
		System.out.println("The big engine is running");
		return horsepower;
	}

}
