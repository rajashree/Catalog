package com.creational.prototype.ex3;
public class Demo {

	public static void main(String[] args) {

		Person person1 = new Person("Fred");
		System.out.println("person 1:" + person1);
		Person person2 = (Person) person1.doClone();
		System.out.println("person 2:" + person2);
		System.out.println("HASH CODE :::"+person1.hashCode());
		System.out.println("HASH CODE :::"+person2.hashCode());

		Dog dog1 = new Dog("Wooof!");
		System.out.println("dog 1:" + dog1);
		Dog dog2 = (Dog) dog1.doClone();
		System.out.println("dog 2:" + dog2);
		
	}

}

