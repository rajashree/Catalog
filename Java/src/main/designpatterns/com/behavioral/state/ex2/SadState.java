package com.behavioral.state.ex2;

//Concrete State
public class SadState implements EmotionalState {

	public String sayGoodbye() {
		return "Bye. Sniff, sniff.";
	}

	public String sayHello() {
		return "Hello. Sniff, sniff.";
	}

}
