package com.structural.proxy.ex1;

public class ProxyDemo {

	public static void main(String[] args) {

		Proxy proxy = new Proxy();

		FastThing fastThing = new FastThing();
		fastThing.sayHello();

		proxy.sayHello();

	}

}
