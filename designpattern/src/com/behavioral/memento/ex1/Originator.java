/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.behavioral.memento.ex1;

/**
 * @author : Rajashree Meganathan
 * @date : 11/21/12
 */
class Originator {
    private String state;
    // The class could also contain additional data that is not part of the
    // state saved in the memento.

    public void set(String state) {
        System.out.println("Originator: Setting state to " + state);
        this.state = state;
    }

    public Originator.Memento saveToMemento() {
        System.out.println("Originator: Saving to Memento.");
        return new Originator.Memento(state);
    }

    public void restoreFromMemento(Originator.Memento memento) {
        state = memento.getSavedState();
        System.out.println("Originator: State after restoring from Memento: " + state);
    }

    public static class Memento {
        private final String state;

        private Memento(String stateToSave) {
            state = stateToSave;
        }

        private String getSavedState() {
            return state;
        }
    }
}
