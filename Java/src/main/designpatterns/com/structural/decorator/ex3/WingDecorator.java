package com.structural.decorator.ex3;

public class WingDecorator extends AnimalDecorator {

	public WingDecorator(Animal animal) {
		super(animal);
	}

	public void describe() {
		animal.describe();
		System.out.println("I have wings.");
		fly();
	}

	public void fly() {
		System.out.println("I can fly.");
	}

}
