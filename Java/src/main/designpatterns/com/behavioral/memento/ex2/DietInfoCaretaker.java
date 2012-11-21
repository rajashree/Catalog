package com.behavioral.memento.ex2;

//caretaker - saves and restores a DietInfo object's state via a memento
//note that DietInfo.Memento isn't visible to the caretaker so we need to cast the memento to Object
public class DietInfoCaretaker {

	Object objMemento;

	public void saveState(DietInfo dietInfo) {
		objMemento = dietInfo.save();
	}

	public void restoreState(DietInfo dietInfo) {
		dietInfo.restore(objMemento);
	}

}
