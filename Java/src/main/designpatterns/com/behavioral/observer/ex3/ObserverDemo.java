/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.behavioral.observer.ex3;

import java.util.Observable;
import java.util.Observer;

/**
 * @author : Rajashree Meganathan
 * @date : 11/21/12
 */
public class ObserverDemo {
  public static void main(String[] args) {
    MyObservable observable = new MyObservable();
    MyObserver1 observer1 = new MyObserver1();
    MyObserver2 observer2 = new MyObserver2();
    observable.addObserver(observer1);
    observable.addObserver(observer2);
    observable.start();
    try {
      Thread.sleep(20000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}

class MyObserver1 implements Observer {
  public void update(Observable o, Object arg) {
    Integer count = (Integer) arg;
    System.out.println("update 1");
  }
}

class MyObserver2 implements Observer {
  public void update(Observable o, Object arg) {
    Integer count = (Integer) arg;
    System.out.println("update 2");
  }
}

class MyObservable extends Observable implements Runnable {
  public MyObservable() {
  }

  public void start() {
    new Thread(this).start();
  }

  public void run() {
    int count = 0;
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    count++;
    setChanged();
    notifyObservers(new Integer(count));
  }
}