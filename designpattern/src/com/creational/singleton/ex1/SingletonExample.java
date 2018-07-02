package com.creational.singleton.ex1;

public class SingletonExample {

	private static SingletonExample singletonExample = null;

	private SingletonExample() {
	}

	public static SingletonExample getInstance() {
		if (singletonExample == null) {
			singletonExample = new SingletonExample();
		}
		return singletonExample;
	}

	public void sayHello() {
		System.out.println("Hello");
	}
}
