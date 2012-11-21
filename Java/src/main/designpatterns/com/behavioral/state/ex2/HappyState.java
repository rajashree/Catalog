package com.behavioral.state.ex2;

//Concrete State
public class HappyState implements EmotionalState {

	public String sayGoodbye() {
		return "Bye, friend!";
	}

	public String sayHello() {
		return "Hello, friend!";
	}

}
