package com.behavioral.observer.ex2;

import java.util.Observable;
import java.util.Observer;

public class ObserverDemo {
  public static void main(String[] argv) throws Exception {
    MyModel model = new MyModel();
    model.addObserver(new Observer() {
      public void update(Observable o, Object arg) {
      }
    });
    model.setChanged();
    Object arg = "new information";
    model.notifyObservers(arg);
  }
}

   
    
  