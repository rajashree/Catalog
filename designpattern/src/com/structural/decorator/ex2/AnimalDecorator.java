package com.structural.decorator.ex2;

public abstract class AnimalDecorator implements Animal {

	Animal animal;

	public AnimalDecorator(Animal animal) {
		this.animal = animal;
	}

}
