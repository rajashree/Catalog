package com.behavioral.command.ex2;

public class Demo {

	public static void main(String[] args) {

		Lunch lunch = new Lunch(); // receiver
		Command lunchCommand = new LunchCommand(lunch); // concrete command

		Dinner dinner = new Dinner(); // receiver
		Command dinnerCommand = new DinnerCommand(dinner); // concrete command

		MealInvoker mealInvoker = new MealInvoker(lunchCommand); // invoker
		mealInvoker.invoke();

		mealInvoker.setCommand(dinnerCommand);
		mealInvoker.invoke();

	}

}
