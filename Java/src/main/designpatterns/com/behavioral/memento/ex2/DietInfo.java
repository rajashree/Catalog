package com.behavioral.memento.ex2;

//originator - object whose state we want to save
public class DietInfo {

	String personName;
	int dayNumber;
	int weight;

	public DietInfo(String personName, int dayNumber, int weight) {
		this.personName = personName;
		this.dayNumber = dayNumber;
		this.weight = weight;
	}

	public String toString() {
		return "Name: " + personName + ", day number: " + dayNumber + ", weight: " + weight;
	}

	public void setDayNumberAndWeight(int dayNumber, int weight) {
		this.dayNumber = dayNumber;
		this.weight = weight;
	}

	public Memento save() {
		return new Memento(personName, dayNumber, weight);
	}

	public void restore(Object objMemento) {
		Memento memento = (Memento) objMemento;
		personName = memento.mementoPersonName;
		dayNumber = memento.mementoDayNumber;
		weight = memento.mementoWeight;
	}

	// memento - object that stores the saved state of the originator
	private class Memento {
		String mementoPersonName;
		int mementoDayNumber;
		int mementoWeight;

		public Memento(String personName, int dayNumber, int weight) {
			mementoPersonName = personName;
			mementoDayNumber = dayNumber;
			mementoWeight = weight;
		}
	}
}

