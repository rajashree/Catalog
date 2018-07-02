package com.structural.decorator.ex2;

public class DecoratorDemo {

	public static void main(String[] args) {

		Animal animal = new LivingAnimal();
		animal.describe();

		animal = new LegDecorator(animal);
		animal.describe();

		animal = new WingDecorator(animal);
		animal.describe();

	}

}
