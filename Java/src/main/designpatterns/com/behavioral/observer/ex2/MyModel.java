/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.behavioral.observer.ex2;

import java.util.Observable;

/**
 * @author : Rajashree Meganathan
 * @date : 11/21/12
 */
class MyModel extends Observable {
  public synchronized void setChanged() {
    super.setChanged();
  }
}
