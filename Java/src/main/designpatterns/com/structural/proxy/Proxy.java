package com.structural.proxy;

import java.util.Date;
/*
 * The Proxy class is a proxy to a SlowThing object. Since a SlowThing object 
 * takes 5 seconds to create, we'll use a proxy to a SlowThing so that a SlowThing 
 * object is only created on demand. This occurs when the proxy's sayHello() method 
 * is executed. It instantiates a SlowThing object if it doesn't already exist and 
 * then calls sayHello() on the SlowThing object. 
 * */
public class Proxy {

	SlowThing slowThing;

	public Proxy() {
		System.out.println("Creating proxy at " + new Date());
	}

	public void sayHello() {
		if (slowThing == null) {
			slowThing = new SlowThing();
		}
		slowThing.sayHello();
	}

}
