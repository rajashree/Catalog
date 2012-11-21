package com.behavioral.state.ex2;

//Context
public class Person implements EmotionalState {

	EmotionalState emotionalState;

	public Person(EmotionalState emotionalState) {
		this.emotionalState = emotionalState;
	}

	public void setEmotionalState(EmotionalState emotionalState) {
		this.emotionalState = emotionalState;
	}

	public String sayGoodbye() {
		return emotionalState.sayGoodbye();
	}

	public String sayHello() {
		return emotionalState.sayHello();
	}

}
