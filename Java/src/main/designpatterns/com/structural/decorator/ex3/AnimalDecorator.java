package com.structural.decorator.ex3;

public abstract class AnimalDecorator implements Animal {

	Animal animal;

	public AnimalDecorator(Animal animal) {
		this.animal = animal;
	}

}
