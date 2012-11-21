package com.structural.decorator.ex3;

public class LegDecorator extends AnimalDecorator {

	public LegDecorator(Animal animal) {
		super(animal);
	}

	public void describe() {
		animal.describe();
		System.out.println("I have legs.");
		dance();
	}

	public void dance() {
		System.out.println("I can dance.");
	}

}
