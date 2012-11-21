package com.behavioral.template;
public abstract class Meal {

	// template method
	public final void doMeal() {
		prepareIngredients();
		cook();
		eat();
		cleanUp();
	}

	public abstract void prepareIngredients();

	public abstract void cook();

	public void eat() {
		System.out.println("Mmm, that's good");
	}

	public abstract void cleanUp();

}
