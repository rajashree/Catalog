package com.creational.builder.ex2;

public interface MealBuilder {
	public void buildDrink();

	public void buildMainCourse();

	public void buildSide();

	public Meal getMeal();
}
