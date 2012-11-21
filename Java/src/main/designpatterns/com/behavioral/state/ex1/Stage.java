/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.behavioral.state.ex1;

/**
 * @author : Rajashree Meganathan
 * @date : 11/21/12
 */
class Stage {
private Actor actor = new HappyActor();

public void change() {
 actor = new SadActor();
}

public void performPlay() {
 actor.act();
}
}
