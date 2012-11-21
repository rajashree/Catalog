package com.behavioral.state.ex2;

public class Demo {

	public static void main(String[] args) {

		Person person = new Person(new HappyState());
		System.out.println("Hello in happy state: " + person.sayHello());
		System.out.println("Goodbye in happy state: " + person.sayGoodbye());

		person.setEmotionalState(new SadState());
		System.out.println("Hello in sad state: " + person.sayHello());
		System.out.println("Goodbye in sad state: " + person.sayGoodbye());

	}

}
