package com.structural.composite.ex2;

public class Leaf implements Component {

	String name;

	public Leaf(String name) {
		this.name = name;
	}

	public void sayHello() {
		System.out.println(name + " leaf says hello");
	}

	public void sayGoodbye() {
		System.out.println(name + " leaf says goodbye");
	}

}

