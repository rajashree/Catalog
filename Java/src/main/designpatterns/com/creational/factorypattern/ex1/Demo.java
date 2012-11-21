package com.creational.factorypattern.ex1;

public class Demo {

	public static void main(String[] args) {
		AnimalFactory animalFactory = new AnimalFactory();

		Animal a1 = animalFactory.getAnimal("feline");
		System.out.println("a1 sound: " + a1.makeSound());

		Animal a2 = animalFactory.getAnimal("canine");
		System.out.println("a2 sound: " + a2.makeSound());
	}

}
