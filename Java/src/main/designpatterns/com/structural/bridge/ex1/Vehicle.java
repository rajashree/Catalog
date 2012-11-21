package com.structural.bridge.ex1;

public abstract class Vehicle {

	Engine engine;
	int weightInKilos;

	public abstract void drive();

	public void setEngine(Engine engine) {
		this.engine = engine;
	}

	public void reportOnSpeed(int horsepower) {
		int ratio = weightInKilos / horsepower;
		if (ratio < 3) {
			System.out.println("The vehicle is going at a fast speed.");
		} else if ((ratio >= 3) && (ratio < 8)) {
			System.out.println("The vehicle is going an average speed.");
		} else {
			System.out.println("The vehicle is going at a slow speed.");
		}
	}

}
