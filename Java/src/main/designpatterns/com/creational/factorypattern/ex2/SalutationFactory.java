package com.creational.factorypattern.ex2;
/*
 * When to use a Factory Pattern?
The Factory patterns can be used in following cases:
1. When a class does not know which class of objects it must create.
2. A class specifies its sub-classes to specify which objects to create.
3. In programmer�s language (very raw form), you can use factory pattern where you 
have to create an object of any one of sub-classes depending on the data provided.
 */
public class SalutationFactory {
	public Person greetPerson(String name, String gender){
		if(gender.equalsIgnoreCase("F")){
			return new Female(name);
		}else if(gender.equalsIgnoreCase("M")){
			return new Male(name);
		}else{
			return null;
		}	
	}
	
	public static void main(String[] args){
		SalutationFactory obj = new SalutationFactory();
		//obj.greetPerson(args[0], args[1]);
		obj.greetPerson("Rajashree","r");
	}

}
